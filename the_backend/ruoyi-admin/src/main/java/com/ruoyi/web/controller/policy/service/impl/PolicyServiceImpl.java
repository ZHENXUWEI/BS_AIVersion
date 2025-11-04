package com.ruoyi.web.controller.policy.service.impl;

import cn.hutool.core.convert.Convert;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.api.domain.dto.PolicyAPIDto;
import com.ruoyi.web.controller.company.domain.Company;
import com.ruoyi.web.controller.company.domain.CompanyIndicatorDictionary;
import com.ruoyi.web.controller.company.domain.CompanyIndicatorRecord;
import com.ruoyi.web.controller.company.mapper.CompanyIndicatorRecordMapper;
import com.ruoyi.web.controller.company.mapper.CompanyMapper;
import com.ruoyi.web.controller.company.service.ICompanyIndicatorDictionaryService;
import com.ruoyi.web.controller.policy.domain.Policy;
import com.ruoyi.web.controller.policy.domain.PolicyModelIndicatorRecord;
import com.ruoyi.web.controller.policy.domain.dto.PolicyExportDto;
import com.ruoyi.web.controller.policy.domain.dto.PolicyModel;
import com.ruoyi.web.controller.policy.mapper.PolicyMapper;
import com.ruoyi.web.controller.policy.mapper.PolicyModelIndicatorRecordMapper;
import com.ruoyi.web.controller.policy.service.IPolicyService;
import com.ruoyi.web.controller.policy.domain.dto.FutureModelExportDto;
//import org.nfunk.jep.JEP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PolicyServiceImpl implements IPolicyService {

    @Autowired
    private PolicyMapper policyMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyIndicatorRecordMapper companyIndicatorRecordMapper;

    @Autowired
    private PolicyModelIndicatorRecordMapper policyModelIndicatorRecordMapper;

    @Autowired
    private ICompanyIndicatorDictionaryService companyIndicatorDictionaryService;

    /**
     * 从一个给定的字符串中提取所有的数字，并将每个匹配的数字及其位置信息存储到一个Result对象的列表中
     *
     * @param str 待解析的字符串
     * @return 数字的位置和值
     */
    private static List<Result> parse(String str) {
        String regex = "\\b(?:\\d+(?:\\.\\d+)?|[\\d.]+(?:[eE][-+]?\\d+)?)\\b"; // 正则表达式，匹配一个或多个连续的数字
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        List<Result> results = new ArrayList<>();
        while (matcher.find()) {
            int position = matcher.start(); // 获取当前匹配的起始位置
            int length = matcher.group().length(); // 获取匹配的数字的长度
            results.add(new Result(matcher.group(), position, length)); // 直接存储匹配的字符串
        }
        return results;
    }

    /**
     * 解析可能由数字+字符串形式组成的文字
     *
     * @param str 待解析的字符串
     * @return 文字的位置和值
     */
    public static List<Result> complexFormulaParser(String str) {
        List<Result> results = new ArrayList<>();

        // 定义正则表达式
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5a-zA-Z0-9]+[\\u4e00-\\u9fa5]+[\\u4e00-\\u9fa5a-zA-Z0-9]*");

        // 使用正则表达式提取匹配项
        Matcher matcher = pattern.matcher(str);

        // 遍历所有匹配项
        while (matcher.find()) {
            // 获取匹配项的值
            String number = matcher.group();

            // 获取匹配项的起始位置
            int position = matcher.start();

            // 获取匹配项的长度
            int length = number.length();

            // 创建 Result 对象并添加到列表中
            results.add(new Result(number, position, length));
        }

        return results;
    }

    /**
     * 运行qlExpress
     *
     * @param express 替换后的公式
     * @return 成功与否
     */
    private static boolean runExpress(String express) {
        ExpressRunner runner = new ExpressRunner(true, false);
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        Object r = null;
        try {
            System.out.println(express);
            r = runner.execute(express, context, null, true, false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (r == null) {
            return false;
        }
        return (boolean) r;
    }

    public static void main(String[] args) {
        List<Result> results = parse("(1&2)|3");

        StringBuilder sb = new StringBuilder("(1&2)|3");
        for (int i = results.size() - 1; i >= 0; i--) {
            Result result = results.get(i);
            String fetchData = "(1=1)";
            sb.replace(result.position, result.position + result.length, fetchData); // 进行替换
        }
        String value = sb.toString()
                .replace("&", "&&")
                .replace("|", "||")
                .replace("=", "==");
        System.out.println(value);
        boolean result = runExpress(value);
        System.out.println(result);
    }

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Policy selectPolicyById(Integer id) {
        return policyMapper.selectPolicyById(id);
    }

    @Override
    public List<Policy> selectPolicyListById(Integer id) {
        return policyMapper.selectPolicyListById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param policy 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Policy> selectPolicyList(Policy policy) {
        List<Policy> policies = policyMapper.selectPolicyList(policy);
        return policies;
    }

    @Override
    public List<Policy> selectCustomModelList(Policy policy) {
        return policyMapper.selectCustomPolicyList(policy);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param policy 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertPolicyModelRecord(Policy policy) {
        //此处逻辑已在前端vue中进行处理
//        String policyDetail = policy.getDocDetail();
//        // 去除尾部空格
//        policyDetail = policyDetail.trim();
//        // 检查字符串末尾是否是分号，如果是，则去掉
//        if (policyDetail.endsWith(";")) {
//            policyDetail = policyDetail.substring(0, policyDetail.length() - 1);
//        }
//        // 使用分号";"作为分隔符来分割字符串
//        String[] parts = policyDetail.split(";");
//        // 计算最后一个分号之后的小字符串数量
//        int count = parts.length;
//        policy.setDocNumber(count);
        return policyMapper.insertPolicyModel(policy);
    }

    public int insertPolicyRecord(Policy policy) {
        return policyMapper.insertPolicy(policy);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param policy 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updatePolicy(Policy policy) {
        return policyMapper.updatePolicy(policy);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deletePolicyByIds(Long[] ids) {
        return policyMapper.deletePolicyByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deletePolicyById(Integer id) {
        return policyMapper.deletePolicyById(id);
    }

    @Override
    public int insertPolicyModelRecord(Integer policyId, Integer dictionaryId, String operator, Double threshold) {
        return policyMapper.insertPolicyModelRecord(policyId, dictionaryId, operator, threshold);
    }

    @Override
    public int updatePolicyFormula(Integer policyId, String formula) {
        return policyMapper.updatePolicyFormula(policyId, formula);
    }

    @Override
    public List<PolicyModel> selectPolicyModelListByPolicyId(Integer policyId) {
        return policyMapper.selectPolicyModelListByPolicyId(policyId);
    }

    @Override
    public List<Company> selectMatchCompany(Integer policyId, String formula) {

        List<CompanyIndicatorRecord> companyIndicatorRecords = companyIndicatorRecordMapper.selectCompanyIndicatorRecordList(null);
        Set<Integer> companyIdSet = new HashSet<>();
        companyIndicatorRecords.forEach(companyIndicatorRecord -> {
            companyIdSet.add(companyIndicatorRecord.getCompanyId());
        });

        List<Company> resultCompanyList = new ArrayList<>();
        companyIdSet.forEach(id -> {
            CompanyIndicatorRecord companyIndicatorRecord = new CompanyIndicatorRecord();
            companyIndicatorRecord.setCompanyId(id);
            List<CompanyIndicatorRecord> templateCompanyIndicatorRecordList = companyIndicatorRecordMapper
                    .selectCompanyIndicatorRecordList(companyIndicatorRecord);
            List<PolicyModelIndicatorRecord> policyModelIndicatorRecords = policyModelIndicatorRecordMapper.selectPolicyModelIndicatorRecordByPolicyId(policyId);
            boolean result = true;
            for (PolicyModelIndicatorRecord pRecord : policyModelIndicatorRecords) {
                if (!result) {
                    break;
                }
                for (CompanyIndicatorRecord cRecord : templateCompanyIndicatorRecordList) {
                    if (cRecord.getDictionaryId() != pRecord.getIndicatorDictionaryId()) {
                        continue;
                    }
                    if (">".equals(pRecord.getOperator()) && !(cRecord.getThreshold() > pRecord.getThreshold())) {
                        result = false;
                        break;
                    } else if ("<".equals(pRecord.getOperator()) && !(cRecord.getThreshold() < pRecord.getThreshold())) {
                        result = false;
                        break;
                    } else if ("=".equals(pRecord.getOperator()) && !(cRecord.getThreshold() == pRecord.getThreshold())) {
                        result = false;
                        break;
                    }
                }
            }
            if (result) {
                resultCompanyList.add(companyMapper.selectCompanyListById(id).get(0));
            }
        });
        return resultCompanyList;
    }

    @Override
    public List<Company> selectMatchCompanyV2(Integer policyId, String formula) {

        List<CompanyIndicatorRecord> companyIndicatorRecords
                = companyIndicatorRecordMapper.selectCompanyIndicatorRecordList(null);
        Set<Integer> companyIdSet = new HashSet<>();
        companyIndicatorRecords.forEach(companyIndicatorRecord -> {
            companyIdSet.add(companyIndicatorRecord.getCompanyId());
        });

        List<Company> resultCompanyList = new ArrayList<>();
        companyIdSet.forEach(id -> {
            boolean result = false;
            String express = expressEngine(id, formula, policyId);
            try {
                result = runExpress(express);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (result) {
                resultCompanyList.add(companyMapper.selectCompanyListById(id).get(0));
            }
        });
        return resultCompanyList;
    }

    @Override
    public List<Company> selectMatchCompanyV3(Integer policyId, String formula) {

        //获取所有的企业指标
        List<CompanyIndicatorRecord> companyIndicatorRecords
                = companyIndicatorRecordMapper.selectCompanyIndicatorRecordList(null);
        Set<Integer> companyIdSet = new HashSet<>();

        //获取所有企业指标指向的企业id
        companyIndicatorRecords.forEach(companyIndicatorRecord -> {
            companyIdSet.add(companyIndicatorRecord.getCompanyId());
        });

        List<Company> resultCompanyList = new ArrayList<>();

        //对上面筛选出来的每个企业进行匹配
        companyIdSet.forEach(id -> {
            boolean result = false;
            String express = expressEngine(id, formula, policyId);
            try {
                result = runExpress(express);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (result) {
                List<Company> companies = companyMapper.selectCompanyListById(id);
                if (!companies.isEmpty()) {
                    resultCompanyList.add(companies.get(0));
                }
            }
        });
        return resultCompanyList;
    }

    @Override
    public List<Company> selectMatchCompanyV4(Integer policyId, String formula) {
        //获取所有的企业指标
        List<CompanyIndicatorRecord> companyIndicatorRecords
                = companyIndicatorRecordMapper.selectCompanyIndicatorRecordList(null);
        Set<Integer> companyIdSet = new HashSet<>();

        //获取所有企业指标指向的企业id
        companyIndicatorRecords.forEach(companyIndicatorRecord -> {
            companyIdSet.add(companyIndicatorRecord.getCompanyId());
        });

        List<Company> resultCompanyList = new ArrayList<>();

        //对上面筛选出来的每个企业进行匹配
        companyIdSet.forEach(id -> {
            boolean result = false;
            boolean complexRes = false;
            Sorter sorter = expressEngineV2(id, formula, policyId);
            try {
                if (StringUtils.isNotEmpty(sorter.getFormula())) {
                    result = runExpress(sorter.getFormula());
                } else {
                    result = true;
                }
                complexRes = complexExpressEngine(sorter.getIds(), id);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (result && complexRes) {
                List<Company> companies = companyMapper.selectCompanyListById(id);
                if (!companies.isEmpty()) {
                    resultCompanyList.add(companies.get(0));
                }
            }
        });
        return resultCompanyList;
    }

    @Override
    public List<Policy> selectPolicyByClass(String policyType, String subPolicyType) {
        return policyMapper.selectPolicyByClass(policyType, subPolicyType);
    }

    @Override
    public List<PolicyModelIndicatorRecord> selectPolicyModelIndicatorRecordByPolicyId(Integer id) {
        return policyModelIndicatorRecordMapper.selectPolicyModelIndicatorRecordByPolicyId(id);
    }

    @Override
    public List<Policy> selectPolicyByName(String policyName) {
        return policyMapper.selectPolicyByName(policyName);
    }

    public List<Policy> selectPolicyByPublishDate() {
        return policyMapper.selectPolicyByPublishDate();
    }

    public List<Policy> selectPolicyByPublishDate1() {
        return policyMapper.selectPolicyByPublishDate1();
    }

    @Override
    public List<Policy> selectPolicyByRec() {
        return policyMapper.selectPolicyByRec();
    }

    @Override
    public List<Policy> selectPolicyByHotLevel() {
        return policyMapper.selectPolicyByHotLevel();
    }

    /**
     * 从政策名称中提取年份，如果无法提取则返回默认年份
     *
     * @param policy      政策对象
     * @param defaultYear 默认年份
     * @return 提取的年份或默认年份
     */
    private Integer extractYearFromPolicy(Policy policy, Integer defaultYear) {
        if (policy.getName() != null && policy.getName().length() >= 4 && isAllDigits(policy.getName().substring(0, 4))) {
            return Integer.parseInt(policy.getName().substring(0, 4));
        }
        return defaultYear;
    }

    /**
     * 根据条件过滤政策列表
     *
     * @param policies 政策列表
     * @param policy   查询条件
     * @param year     年份
     * @return 过滤后的政策列表
     */
    private List<Policy> filterPolicies(List<Policy> policies, Policy policy, Integer year) {
        if (policy.getId() != null) {
            // 单个导出情况
            return policies.stream()
                    .filter(policyDB -> policyDB.getId().equals(policy.getId()))
                    .collect(Collectors.toList());
        } else {
            // 全部导出情况
            return policies.stream()
                    .filter(policyDB -> policyDB.getName() != null && policyDB.getName().startsWith(String.valueOf(year)))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Policy> getCurrentYearCustomModelList(Policy policy) {
        Integer year = extractYearFromPolicy(policy, LocalDate.now().getYear());
        List<Policy> policies = selectCustomModelList(new Policy(1));
        return filterPolicies(policies, policy, year);
    }

    @Override
    public List<Policy> getFutureYearCustomModelList(Policy policy) {
        Integer year = extractYearFromPolicy(policy, LocalDate.now().getYear() + 1);
        List<Policy> policies = selectCustomModelList(new Policy(1));
        return filterPolicies(policies, policy, year);
    }

    /**
     * 把任意一条cRecord转成运算公式
     *
     * @param id        dictionary_id
     * @param companyId company_id
     * @return 单一的运算公式
     */
    private String fetch(Integer id, Integer companyId, Integer policyId) {

        CompanyIndicatorDictionary companyIndicatorDictionary = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryById(id);
        Policy policy = policyMapper.selectPolicyById(policyId);
        Integer year = Integer.parseInt(policy.getName().substring(0, 4));
        //根据政策模型年份和dictionaryId查找对应政策模型年份的该年此公司的指标值
        List<CompanyIndicatorRecord> companyIndicatorRecords = companyIndicatorRecordMapper.selectCompanyIndicatorRecordListByCompanyIdAndYearV1(id, year);

        //通过companyId找出属于目标公司的指标值
        CompanyIndicatorRecord cRecord = new CompanyIndicatorRecord();
        for (CompanyIndicatorRecord companyIndicatorRecord : companyIndicatorRecords) {
            if (companyIndicatorRecord.getCompanyId() == companyId) {
                cRecord = companyIndicatorRecord;
                break;
            }
        }

        //找出对应政策模型要求该指标的值
        PolicyModelIndicatorRecord pRecord = policyModelIndicatorRecordMapper
                .selectPolicyModelIndicatorRecord(policyId, id);

        if ("single".equals(companyIndicatorDictionary.getType())) {
            if (compareOptions(cRecord, pRecord, 0)) {
                return "true";
            } else {
                return "false";
            }
        } else if ("multiple".equals(companyIndicatorDictionary.getType())) {
            if (compareOptions(cRecord, pRecord, 1)) {
                return "true";
            } else {
                return "false";
            }
        } else if ("date".equals(companyIndicatorDictionary.getType())) {
            if (isWithinTimeRange(cRecord, pRecord)) {
                return "true";
            } else {
                return "false";
            }
        }

        //查看对应政策模型要求的指标是否有应用复杂公式
        String complexFormula = pRecord.getComplexFormula();

        if (complexFormula != null && !complexFormula.isEmpty()) {
            List<Result> results = parse(complexFormula);
            StringBuilder sb = new StringBuilder(complexFormula);
            for (int i = 0; i <= results.size() - 1; i++) {
                int dictionaryId = Integer.parseInt(results.get(i).number);
                CompanyIndicatorRecord companyIndicatorRecord = companyIndicatorRecordMapper.selectCompanyIndicatorRecordList(new CompanyIndicatorRecord(null, companyId, null, dictionaryId, null))
                        .get(0);
                Double threshold = companyIndicatorRecord.getThreshold();
                sb.replace(results.get(i).position, results.get(i).position + results.get(i).length, threshold.toString()); // 进行替换
                results = parse(sb.toString());
            }
            return "(" + sb.toString() + pRecord.getOperator() + pRecord.getThreshold() + ")";
        } else {
            //若公司在对应政策模型年份有指标值，且该年政策模型该指标值存在，则转换为目标公式
            if (pRecord.getId() != null && cRecord.getId() != null) {
                return "(" + cRecord.getThreshold().toString()
                        + pRecord.getOperator()
                        + pRecord.getThreshold().toString() + ")";
            } else if (pRecord.getId() == null) {
                return "true";
            }
            return "false";
        }
    }


    /**
     * 处理单选/多选指标是否匹配
     *
     * @param cRecord 公司指标
     * @param pRecord 模型要求指标
     * @param type    1-多选/0-单选
     * @return 是否符合标准
     */
    private boolean compareOptions(CompanyIndicatorRecord cRecord, PolicyModelIndicatorRecord pRecord, int type) {
        String cRemark = cRecord.getRemark();
        String pRemark = pRecord.getRemark();

        String[] cOptions = cRemark.split("[；;]");
        String[] pOptions = pRemark.split("[；;]");

        Integer indicatorDictionaryId = pRecord.getIndicatorDictionaryId();
        CompanyIndicatorDictionary cid = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryById(indicatorDictionaryId);
        String norm = extractNumberStr(cid.getName());
        Set<String> cSet = new HashSet<>(Arrays.asList(cOptions));
        Set<String> pSet = new HashSet<>(Arrays.asList(pOptions));
        if (type == 0 || (type == 1 && !StringUtils.isNotEmpty(norm))) {
            return cSet.containsAll(pSet);
        } else if (type == 1) {
            if (norm != null) {
                int num = Convert.chineseToNumber(norm);
                return checkSets(cSet, pSet, num);
            }
        }
        return false;
    }

    /**
     * 处理时间段指标是否符合标准
     *
     * @param cRecord 公司指标
     * @param pRecord 模型要求指标
     * @return 是否符合标准
     */
    private boolean isWithinTimeRange(CompanyIndicatorRecord cRecord, PolicyModelIndicatorRecord pRecord) {
        String cRemark = cRecord.getRemark();
        String pRemark = pRecord.getRemark();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            // 使用正则表达式分割字符串
            String[] parts = pRemark.split("(?<=\\d{2}:\\d{2}:\\d{2})-(?=\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid date range format");
            }

            // 解析时间点
            Date startTime = formatter.parse(parts[0].trim());
            Date endTime = formatter.parse(parts[1].trim());
            Date checkTime = formatter.parse(cRemark.trim());

            // 比较时间
            return !checkTime.before(startTime) && !checkTime.after(endTime);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
    }

    /**
     * 针对多选形似"任意...x个"结构对汉字数字x的剥离
     *
     * @param str 待处理字符串
     * @return 中文汉字
     */
    private String extractNumberStr(String str) {
        if (!str.contains("任意")) {
            return null;
        }
        String regex = "任意(\\d+|[一二三四五六七八九十百千万亿]+)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 判断cSet是否至少满足num个项于cSet内
     *
     * @param cSet 公司指标
     * @param pSet 模型要求指标
     * @param num  满足数
     * @return 是否满足
     */
    private boolean checkSets(Set<String> cSet, Set<String> pSet, int num) {
        int count = 0;

        for (String item : cSet) {
            if (pSet.contains(item)) {
                count++;
            }

            if (count >= num) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否全为数字字符
     *
     * @param str 目标字符串
     * @return true/false
     */
    public static boolean isAllDigits(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 替换公式的索引为具体要求
     *
     * @param companyId 公司Id
     * @param formula   匹配公式
     * @param policyId  政策Id
     * @return 替换后的结果
     */
    private String expressEngine(Integer companyId, String formula, Integer policyId) {

        //解析匹配公式
        List<Result> results = parse(formula);

        //构建组装器
        StringBuilder sb = new StringBuilder(formula);

        //对公式从后往前替换
        for (int i = results.size() - 1; i >= 0; i--) {
            Result result = results.get(i);
            String fetchData;
            try {
                Integer indicatorDictionaryId = policyModelIndicatorRecordMapper.selectPolicyModelIndicatorRecordById(Integer.valueOf(result.number)).getIndicatorDictionaryId();

                //得到替换的公式
                fetchData = fetch(indicatorDictionaryId, companyId, policyId);

                //对公式进行替换
                sb.replace(result.position, result.position + result.length, fetchData); // 进行替换
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("~~~~~~~~~~~~最后匹配的公式：" + sb.toString() + "~~~~~~~~~~~~");
        return sb.toString().replace("&", "&&").replace("|", "||");
    }

    /**
     * 替换公式的索引为具体要求，且将复杂公式剥离
     *
     * @param companyId 公司Id
     * @param formula   匹配公式
     * @param policyId  政策Id
     * @return 替换后的结果和复杂公式的指标索引
     */
    private Sorter expressEngineV2(Integer companyId, String formula, Integer policyId) {

        Sorter sorter = new Sorter();

        //解析匹配公式
        List<Result> results = parse(formula);

        //构建组装器
        StringBuilder sb = new StringBuilder(formula);

        //对公式从后往前替换
        for (int i = results.size() - 1; i >= 0; i--) {
            Result result = results.get(i);
            PolicyModelIndicatorRecord pmir = policyModelIndicatorRecordMapper.selectPolicyModelIndicatorRecordById(Integer.valueOf(result.number));
            if (pmir != null && "0".equals(pmir.getType())) {
                String fetchData;
                try {
                    Integer indicatorDictionaryId = policyModelIndicatorRecordMapper.selectPolicyModelIndicatorRecordById(Integer.valueOf(result.number)).getIndicatorDictionaryId();

                    //得到替换的公式
                    fetchData = fetch(indicatorDictionaryId, companyId, policyId);

                    //对公式进行替换
                    sb.replace(result.position, result.position + result.length, fetchData); // 进行替换
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (pmir != null && "1".equals(pmir.getType())) {
                sorter.getIds().add(String.valueOf(pmir.getId()));
                if (result.position == 0 && results.size() < 1) {
                    sb.replace(result.position, result.position + result.length, "");
                } else if (result.position == 0) {
                    sb.replace(result.position, result.position + result.length + 1, "");
                } else {
                    sb.replace(result.position - 1, result.position + result.length + 1, "");
                }
            }
        }
        System.out.println("~~~~~~~~~~~~最后匹配的公式：" + sb.toString() + "~~~~~~~~~~~~");
        sorter.setFormula(sb.toString().replace("&", "&&").replace("|", "||"));
        return sorter;
    }

    /**
     * @param ids       指标id
     * @param companyId 企业id
     * @return 复杂公式是否全为true
     */
    private boolean complexExpressEngine(List<String> ids, Integer companyId) {
        //替换
        for (String id : ids) {
            PolicyModelIndicatorRecord pmir = policyModelIndicatorRecordMapper.selectPolicyModelIndicatorRecordById(Integer.valueOf(id));
            List<Result> results = complexFormulaParser(pmir.getComplexFormula());
            //构建组装器
            StringBuilder sb = new StringBuilder(pmir.getComplexFormula());
            for (int i = results.size() - 1; i >= 0; i--) {
                Result result = results.get(i);
                CompanyIndicatorDictionary companyIndicatorDictionary = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryByName(result.number);
                Integer year = Integer.parseInt(companyIndicatorDictionary.getName().substring(0, 4));
                List<CompanyIndicatorRecord> companyIndicatorRecords = companyIndicatorRecordMapper.selectCompanyIndicatorRecordListByCompanyIdAndYearV2(companyIndicatorDictionary.getId(), companyId, year);
                if (companyIndicatorRecords.size() < 1) {
                    return false;
                }
                sb.replace(result.position, result.position + result.length, String.valueOf(companyIndicatorRecords.get(0).getThreshold())); // 进行替换
            }
            sb.append(pmir.getOperator()).append(pmir.getThreshold());
            boolean b = runExpress(sb.toString());
            if (!b) {
                return false;
            }
        }
        return true;
    }

    private StringBuilder complexFormulaBuilder(String indicatorId, Integer companyId) {
        PolicyModelIndicatorRecord pmir = policyModelIndicatorRecordMapper.selectPolicyModelIndicatorRecordById(Integer.valueOf(indicatorId));
        List<Result> results = complexFormulaParser(pmir.getComplexFormula());
        //构建组装器
        StringBuilder sb = new StringBuilder(pmir.getComplexFormula());
        for (int i = results.size() - 1; i >= 0; i--) {
            Result result = results.get(i);
            CompanyIndicatorDictionary companyIndicatorDictionary = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryByName(result.number);
            Integer year = Integer.parseInt(companyIndicatorDictionary.getName().substring(0, 4));
            List<CompanyIndicatorRecord> companyIndicatorRecords = companyIndicatorRecordMapper.selectCompanyIndicatorRecordListByCompanyIdAndYearV2(companyIndicatorDictionary.getId(), companyId, year);
            if (companyIndicatorRecords.size() < 1) {
                sb.replace(result.position, result.position + result.length, "0");
            } else {
                sb.replace(result.position, result.position + result.length, String.valueOf(companyIndicatorRecords.get(0).getThreshold())); // 进行替换
            }
        }
        return sb;
    }

    /**
     * 旧版匹配逻辑
     *
     * @param companyId 企业id
     * @param policy    政策模型
     * @param year      年份
     * @return 不匹配的指标和内容
     */
    @Override
    public List<Map<String, String>> c2cNoMatchedIndicator(Integer companyId, Policy policy, Integer year) {
        String formula = policy.getFormula(); //formula的数字代表pmir的id
        List<Map<String, String>> result = new ArrayList<>();
        if (formula != null) {
            List<Result> parse = parse(formula);
            for (Result r : parse) {
                PolicyModelIndicatorRecord policyModelIndicatorRecord = policyModelIndicatorRecordMapper
                        .selectPolicyModelIndicatorRecordById(Integer.valueOf(r.number));
                System.out.println("-----------r.number=" + r.number);
                System.out.println("-----------r.length=" + r.length);
                System.out.println("-----------r.position=" + r.position);
                System.out.println(policyModelIndicatorRecord);
                Integer indicatorDictionaryId = policyModelIndicatorRecord.getIndicatorDictionaryId();
                String type = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryById(indicatorDictionaryId).getType();

                //拿到所有年份不匹配，指标为空的公司指标信息
                List<CompanyIndicatorRecord> companyIndicatorRecordList = companyIndicatorRecordMapper
                        .selectCompanyIndicatorRecordList(new CompanyIndicatorRecord(companyId, policyModelIndicatorRecord.getIndicatorDictionaryId(), year));
                List<CompanyIndicatorRecord> companyIndicatorRecords = new ArrayList<>();

                //拿到所有年份匹配，指标不为空的公司指标信息
                for (CompanyIndicatorRecord companyIndicatorRecord : companyIndicatorRecordList) {
                    if (companyIndicatorRecord.getYear().equals(year) && companyIndicatorRecord.getThreshold() != null) {
                        companyIndicatorRecords.add(companyIndicatorRecord);
                    }
                }
                if (!companyIndicatorRecords.isEmpty()) {
                    String complexFormula = policyModelIndicatorRecord.getComplexFormula();
                    if (complexFormula == null || complexFormula.equals("")) {
                        //假如没有复杂公式
                        CompanyIndicatorRecord cRecord = companyIndicatorRecords.get(0);
                        boolean isSuccess = false;
                        String pValue = policyModelIndicatorRecord.getRemark();
                        String cValue = cRecord.getRemark();
                        if ("text".equals(type)) {
                            if (policyModelIndicatorRecord.getThreshold() != null) {
                                cValue = cRecord.getThreshold().toString();
                                pValue = policyModelIndicatorRecord.getThreshold().toString();
                            }
                            String stringBuilder = cValue
                                    + policyModelIndicatorRecord.getOperator()
                                    + pValue;
                            isSuccess = runExpress(stringBuilder);
                        } else if ("single".equals(type)) {
                            isSuccess = compareOptions(cRecord, policyModelIndicatorRecord, 0);
                        } else if ("multiple".equals(type)) {
                            isSuccess = compareOptions(cRecord, policyModelIndicatorRecord, 1);
                        } else if ("date".equals(type)) {
                            isSuccess = isWithinTimeRange(cRecord, policyModelIndicatorRecord);
                        }
                        if (!isSuccess) {
                            StringBuilder expectSb = new StringBuilder();
                            CompanyIndicatorDictionary companyIndicatorDictionary = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryById(indicatorDictionaryId);
                            expectSb.append(companyIndicatorDictionary.getName())
                                    .append(policyModelIndicatorRecord.getOperator())
                                    .append(pValue);
                            Map<String, String> map = new HashMap<>();
                            map.put("expectIndicator", expectSb.toString());
                            String yourSb = companyIndicatorDictionary.getName()
                                    + "="
                                    + cValue;
                            map.put("yourIndicator", yourSb);
                            result.add(map);
                        }
                    } else {
                        List<Result> parseComplex = parse(complexFormula);
                        StringBuilder sb = new StringBuilder();
                        Double yourIndicatorValue = 0d;
                        Map<String, String> map = new HashMap<>();
                        StringBuilder indicatorName = new StringBuilder();
                        for (Result resultComplex : parseComplex) {
                            indicatorName.append(companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryById(Integer.valueOf(resultComplex.number)).getName()).append("+");
                            List<CompanyIndicatorRecord> cirs = companyIndicatorRecordMapper.selectCompanyIndicatorRecordList(new CompanyIndicatorRecord(companyId, Integer.valueOf(resultComplex.number)));
                            if (!cirs.isEmpty()) {
                                if (cirs.get(0).getThreshold() != null) {
                                    yourIndicatorValue += cirs.get(0).getThreshold();
                                }
                            }
                        }
                        indicatorName.setLength(indicatorName.length() - 1);
                        sb.append(yourIndicatorValue)
                                .append(policyModelIndicatorRecord.getOperator())
                                .append(policyModelIndicatorRecord.getThreshold());
                        boolean isSuccess = runExpress(sb.toString());
                        if (!isSuccess) {
                            String expectSb = indicatorName
                                    + policyModelIndicatorRecord.getOperator()
                                    + policyModelIndicatorRecord.getThreshold();
                            map.put("expectIndicator", expectSb);
                            String yourSb = indicatorName
                                    + "="
                                    + yourIndicatorValue;
                            map.put("yourIndicator", yourSb);
                            result.add(map);
                        }
                    }
                } else {
                    //若指标集为空则返回目标指标全为NULL的信息
                    Map<String, String> map = new HashMap<>();
                    StringBuilder expectSb = new StringBuilder();
                    CompanyIndicatorDictionary companyIndicatorDictionary = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryById(policyModelIndicatorRecord.getIndicatorDictionaryId());
                    expectSb.append(companyIndicatorDictionary.getName())
                            .append(policyModelIndicatorRecord.getOperator());
                    if (policyModelIndicatorRecord.getThreshold() != null) {
                        expectSb.append(policyModelIndicatorRecord.getThreshold());
                    } else {
                        expectSb.append(policyModelIndicatorRecord.getRemark());
                    }
                    map.put("expectIndicator", expectSb.toString());
                    map.put("yourIndicator", "NULL");
                    result.add(map);
                }
            }
        }
        return result;
    }

    @Override
    public List<Map<String, String>> c2cNoMatchedIndicatorV2(Integer companyId, Policy policy, Integer year) {
        String formula = policy.getFormula(); //formula的数字代表pmir的id
        List<Map<String, String>> result = new ArrayList<>();
        if (formula != null) {
            List<Result> parse = parse(formula);
            for (Result r : parse) {
                // 得到pmir记录
                PolicyModelIndicatorRecord policyModelIndicatorRecord = policyModelIndicatorRecordMapper
                        .selectPolicyModelIndicatorRecordById(Integer.valueOf(r.number));
                System.out.println("-----------r.number=" + r.number);
                System.out.println("-----------r.length=" + r.length);
                System.out.println("-----------r.position=" + r.position);
                System.out.println(policyModelIndicatorRecord);
                String indicatorType = policyModelIndicatorRecord.getType();

                boolean isComplexNULL = true;
                if ("1".equals(indicatorType)) {
                    String complexFormula = policyModelIndicatorRecord.getComplexFormula();
                    List<String> list = extractTextGroups(complexFormula);
                    for (String s : list) {
                        CompanyIndicatorDictionary cid = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryByName(s);
                        int indicatorYear = Integer.parseInt(s.substring(0, 4));
                        List<CompanyIndicatorRecord> companyIndicatorRecords = companyIndicatorRecordMapper.selectCompanyIndicatorRecordListByCompanyIdAndYearV2(cid.getId(), companyId, indicatorYear);
                        if (companyIndicatorRecords.size() > 0) {
                            isComplexNULL = false;
                            break;
                        }
                    }
                }

                //得到indicatorDictionaryId
                Integer indicatorDictionaryId = policyModelIndicatorRecord.getIndicatorDictionaryId();
                String type = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryById(indicatorDictionaryId).getType();

                //拿到所有年份不匹配，指标为空的公司指标信息
                List<CompanyIndicatorRecord> companyIndicatorRecordList = companyIndicatorRecordMapper
                        .selectCompanyIndicatorRecordList(new CompanyIndicatorRecord(companyId, policyModelIndicatorRecord.getIndicatorDictionaryId(), year));
                List<CompanyIndicatorRecord> companyIndicatorRecords = new ArrayList<>();

                //拿到所有年份匹配，指标不为空的公司指标信息
                for (CompanyIndicatorRecord companyIndicatorRecord : companyIndicatorRecordList) {
                    if (companyIndicatorRecord.getYear().equals(year) && (companyIndicatorRecord.getThreshold() != null || companyIndicatorRecord.getRemark() != null)) {
                        companyIndicatorRecords.add(companyIndicatorRecord);
                    }
                }
                if (!companyIndicatorRecords.isEmpty() || !isComplexNULL) {
                    String isComplex = policyModelIndicatorRecord.getType();
                    if ("0".equals(isComplex)) {
                        //假如没有复杂公式
                        CompanyIndicatorRecord cRecord = companyIndicatorRecords.get(0);
                        boolean isSuccess = false;
                        String pValue = policyModelIndicatorRecord.getRemark();
                        String cValue = cRecord.getRemark();
                        if ("text".equals(type)) {
                            if (policyModelIndicatorRecord.getThreshold() != null) {
                                if (cRecord.getThreshold() != null) {
                                    cValue = cRecord.getThreshold().toString();
                                    pValue = policyModelIndicatorRecord.getThreshold().toString();
                                    String stringBuilder = cValue
                                            + policyModelIndicatorRecord.getOperator()
                                            + pValue;
                                    isSuccess = runExpress(stringBuilder);
                                }
                            } else if (StringUtils.isNotEmpty(pValue) && StringUtils.isNotEmpty(cValue)) {
                                isSuccess = pValue.equals(cValue);
                            }
                        } else if (cRecord != null && cValue != null) {
                            if ("single".equals(type)) {
                                isSuccess = compareOptions(cRecord, policyModelIndicatorRecord, 0);
                            } else if ("multiple".equals(type)) {
                                isSuccess = compareOptions(cRecord, policyModelIndicatorRecord, 1);
                            } else if ("date".equals(type)) {
                                isSuccess = isWithinTimeRange(cRecord, policyModelIndicatorRecord);
                            }
                        }
                        if (!isSuccess) {
                            StringBuilder expectSb = new StringBuilder();
                            CompanyIndicatorDictionary companyIndicatorDictionary = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryById(indicatorDictionaryId);
                            expectSb.append(companyIndicatorDictionary.getName())
                                    .append(policyModelIndicatorRecord.getOperator())
                                    .append(pValue);
                            Map<String, String> map = new HashMap<>();
                            map.put("expectIndicator", expectSb.toString());
                            String yourSb = companyIndicatorDictionary.getName()
                                    + "="
                                    + cValue;
                            map.put("yourIndicator", yourSb);
                            result.add(map);
                        }
                    } else if ("1".equals(isComplex)) {
                        String complexFormula = policyModelIndicatorRecord.getComplexFormula();
                        StringBuilder sb = complexFormulaBuilder(String.valueOf(policyModelIndicatorRecord.getId()), companyId);
//                        JEP parser = new JEP();
//                        parser.parseExpression(sb.toString());
//                        Double yourIndicatorValue = parser.getValue();
                        ExpressRunner runner = new ExpressRunner(true, false);
                        Object yourIndicatorValue = null;
                        try {
                            String s = sb.toString();
                            s = s.replace("[", "(");
                            s = s.replace("]", ")");
                            yourIndicatorValue = runner.execute(s, null, null, true, false);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
//                            throw new RuntimeException(e);
                        }
//                        DecimalFormat df = new DecimalFormat("#.##");
//                        System.out.println("格式为-----------" + df.format(yourIndicatorValue));
                        Map<String, String> map = new HashMap<>();
                        StringBuilder indicatorName = new StringBuilder();
                        indicatorName.append(complexFormula);
                        sb.append(yourIndicatorValue)
                                .append(policyModelIndicatorRecord.getOperator())
                                .append(policyModelIndicatorRecord.getThreshold());
                        boolean isSuccess = runExpress(sb.toString());
                        if (!isSuccess) {
                            String expectSb = indicatorName
                                    + policyModelIndicatorRecord.getOperator()
                                    + policyModelIndicatorRecord.getThreshold();
                            map.put("expectIndicator", expectSb);
                            String yourSb = indicatorName
                                    + "="
                                    + yourIndicatorValue;
                            map.put("yourIndicator", yourSb);
                            result.add(map);
                        }
                    }
                } else {
                    //若指标集为空则返回目标指标全为NULL的信息
                    Map<String, String> map = new HashMap<>();
                    StringBuilder expectSb = new StringBuilder();
                    if ("0".equals(policyModelIndicatorRecord.getType())) {
                        CompanyIndicatorDictionary companyIndicatorDictionary = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryById(policyModelIndicatorRecord.getIndicatorDictionaryId());
                        expectSb.append(companyIndicatorDictionary.getName())
                                .append(policyModelIndicatorRecord.getOperator());
                        if (policyModelIndicatorRecord.getThreshold() != null) {
                            expectSb.append(policyModelIndicatorRecord.getThreshold());
                        } else {
                            expectSb.append(policyModelIndicatorRecord.getRemark());
                        }
                    } else if ("1".equals(policyModelIndicatorRecord.getType())) {
                        expectSb.append(policyModelIndicatorRecord.getComplexFormula()).append(policyModelIndicatorRecord.getOperator()).append(policyModelIndicatorRecord.getThreshold());
                    }
                    map.put("expectIndicator", expectSb.toString());
                    map.put("yourIndicator", "NULL");
                    result.add(map);
                }
            }
        }
        return result;
    }

    public static List<String> extractTextGroups(String str) {
        List<String> textGroups = new ArrayList<>();

        // 定义正则表达式
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5a-zA-Z0-9]+[\\u4e00-\\u9fa5]+[\\u4e00-\\u9fa5a-zA-Z0-9]*");

        // 使用正则表达式提取匹配项
        Matcher matcher = pattern.matcher(str);

        // 遍历所有匹配项
        while (matcher.find()) {
            // 获取匹配项的值
            String group = matcher.group();
            textGroups.add(group);
        }

        return textGroups;
    }

    /**
     * 获取不满足条件的指标字典ID列表
     *
     * @param companyId 公司ID
     * @param policy    政策对象
     * @param year      年份
     * @return 不满足条件的指标字典ID列表
     */
    public List<Integer> getUnmatchedDictionaryIds(Integer companyId, Policy policy, Integer year) {
        String formula = policy.getFormula();
        List<Integer> unmatchedIds = new ArrayList<>();
        if (formula != null) {
            List<Result> parse = parse(formula);
            for (Result r : parse) {
                PolicyModelIndicatorRecord policyModelIndicatorRecord = policyModelIndicatorRecordMapper
                        .selectPolicyModelIndicatorRecordById(Integer.valueOf(r.number));
                List<CompanyIndicatorRecord> companyIndicatorRecordList = companyIndicatorRecordMapper
                        .selectCompanyIndicatorRecordList(new CompanyIndicatorRecord(companyId, policyModelIndicatorRecord.getIndicatorDictionaryId(), year));
                List<CompanyIndicatorRecord> companyIndicatorRecords = new ArrayList<>();
                for (CompanyIndicatorRecord companyIndicatorRecord : companyIndicatorRecordList) {
                    if (companyIndicatorRecord.getYear().equals(year) && companyIndicatorRecord.getThreshold() != null) {
                        companyIndicatorRecords.add(companyIndicatorRecord);
                    }
                }
                if (!companyIndicatorRecords.isEmpty()) {
                    String complexFormula = policyModelIndicatorRecord.getComplexFormula();
                    if (complexFormula == null || complexFormula.equals("")) {
                        String stringBuilder = companyIndicatorRecords.get(0).getThreshold()
                                + policyModelIndicatorRecord.getOperator()
                                + policyModelIndicatorRecord.getThreshold();
                        boolean isSuccess = runExpress(stringBuilder);
                        if (!isSuccess) {
                            unmatchedIds.add(policyModelIndicatorRecord.getIndicatorDictionaryId());
                        }
                    } else {
                        List<Result> parseComplex = parse(complexFormula);
                        StringBuilder sb = new StringBuilder();
                        Double yourIndicatorValue = 0d;
                        for (Result resultComplex : parseComplex) {
                            List<CompanyIndicatorRecord> cirs = companyIndicatorRecordMapper
                                    .selectCompanyIndicatorRecordList(new CompanyIndicatorRecord(companyId, Integer.valueOf(resultComplex.number)));
                            if (!cirs.isEmpty() && cirs.get(0).getThreshold() != null) {
                                yourIndicatorValue += cirs.get(0).getThreshold();
                            }
                        }
                        sb.append(yourIndicatorValue)
                                .append(policyModelIndicatorRecord.getOperator())
                                .append(policyModelIndicatorRecord.getThreshold());
                        boolean isSuccess = runExpress(sb.toString());
                        if (!isSuccess) {
                            unmatchedIds.add(policyModelIndicatorRecord.getIndicatorDictionaryId());
                        }
                    }
                } else {
                    unmatchedIds.add(policyModelIndicatorRecord.getIndicatorDictionaryId());
                }
            }
        }
        return unmatchedIds;
    }

    static class Result {

        String number; // 存储数字，可能是整数或小数
        int position;
        int length;

        public Result(String number, int position, int length) {
            this.number = number;
            this.position = position;
            this.length = length;
        }
    }

    private static class Sorter {
        private List<String> ids = new ArrayList<>();
        private String formula;

        public List<String> getIds() {
            return ids;
        }

        public void setIds(List<String> ids) {
            this.ids = ids;
        }

        public String getFormula() {
            return formula;
        }

        public void setFormula(String formula) {
            this.formula = formula;
        }

        public Sorter(List<String> ids, String formula) {
            this.ids = ids;
            this.formula = formula;
        }

        public Sorter() {
        }
    }

    @Override
    public Map<String, String> getCompanyStatus(Integer companyId, Policy policy, Integer currentYear, Integer lastYear) {
        Map<String, String> result = new HashMap<>();

        // 获取当前年份的不匹配指标
        List<Map<String, String>> currentYearIndicators = c2cNoMatchedIndicatorV2(companyId, policy, currentYear);
        // 获取上一年份的不匹配指标
        List<Map<String, String>> lastYearIndicators = c2cNoMatchedIndicatorV2(companyId, policy, lastYear);

        boolean currentYearMatch = currentYearIndicators.isEmpty();
        boolean lastYearMatch = lastYearIndicators.isEmpty();

        // 判断企业类型
        if (lastYearMatch && currentYearMatch) {
            result.put("stockCompany", "去年达标，今年达标");
        } else if (lastYearMatch && !currentYearMatch) {
            result.put("retreatCompany", "去年达标，今年未达标");
        } else if (!lastYearMatch && currentYearMatch) {
            result.put("cultivateCompany", "去年未达标，今年达标");
        }

        return result;
    }

    @Override
    public void export(HttpServletResponse response, Policy policy) throws IOException {
        List<Policy> list;
        if (policy.getId() != null) {
            // 导出单个政策模型数据
            list = getCurrentYearCustomModelList(new Policy(policy.getId()));
        } else {
            // 导出所有政策模型数据
            list = getCurrentYearCustomModelList(new Policy());
        }

        // 获取当前年份和上一年份
        int currentYear = LocalDate.now().getYear();
        int lastYear = currentYear - 1;

        // 转换为导出DTO
        List<PolicyExportDto> exportList = list.stream()
                .map(p -> {
                    PolicyExportDto dto = PolicyExportDto.fromPolicy(p);

                    // 获取所有企业的达标状态
                    List<Company> companies = companyMapper.selectCompanyList(null);
                    StringBuilder conditionBuilder = new StringBuilder();

                    for (Company company : companies) {
                        Map<String, String> status = getCompanyStatus(company.getId(), p, currentYear, lastYear);

                        // 添加企业到对应分类
                        if (status.containsKey("stockCompany")) {
                            dto.setStockCompany((dto.getStockCompany() == null ? "" : dto.getStockCompany() + "、")
                                    + company.getCompanyName());
                        }
                        if (status.containsKey("cultivateCompany")) {
                            dto.setCultivateCompany((dto.getCultivateCompany() == null ? "" : dto.getCultivateCompany() + "、")
                                    + company.getCompanyName());
                        }
                        if (status.containsKey("retreatCompany")) {
                            dto.setRetreatCompany((dto.getRetreatCompany() == null ? "" : dto.getRetreatCompany() + "、")
                                    + company.getCompanyName());
                        }

                        // 获取企业的具体指标情况
                        List<Map<String, String>> currentYearIndicators = c2cNoMatchedIndicatorV2(company.getId(), p, currentYear);
                        List<Map<String, String>> lastYearIndicators = c2cNoMatchedIndicatorV2(company.getId(), p, lastYear);

                        // 添加企业的达标情况到政策条件
                        if (!currentYearIndicators.isEmpty() || !lastYearIndicators.isEmpty()) {
                            conditionBuilder.append("【").append(company.getCompanyName()).append("】\n");

                            if (!lastYearIndicators.isEmpty()) {
                                conditionBuilder.append(lastYear).append("年未达标指标：\n");
                                for (Map<String, String> indicator : lastYearIndicators) {
                                    conditionBuilder.append("期望：").append(indicator.get("expectIndicator"))
                                            .append("，实际：").append(indicator.get("yourIndicator").equals("NULL") ? "当年数据未填写" : indicator.get("yourIndicator")).append("\n");
                                }
                            }

                            if (!currentYearIndicators.isEmpty()) {
                                conditionBuilder.append(currentYear).append("年未达标指标：\n");
                                for (Map<String, String> indicator : currentYearIndicators) {
                                    conditionBuilder.append("期望：").append(indicator.get("expectIndicator"))
                                            .append("，实际：").append(indicator.get("yourIndicator").equals("NULL") ? "当年数据未填写" : indicator.get("yourIndicator")).append("\n");
                                }
                            }

                            conditionBuilder.append("\n");
                        }
                    }

                    // 设置政策条件
                    dto.setPolicyCondition(conditionBuilder.toString());

                    return dto;
                })
                .collect(Collectors.toList());

        String filename = URLEncoder.encode("政策模型数据", "UTF-8");

        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xlsx");

        ExcelUtil<PolicyExportDto> util = new ExcelUtil<>(PolicyExportDto.class);
        util.exportExcel(response, exportList, "政策模型数据");
    }

    @Override
    public void exportFutureModel(HttpServletResponse response, Policy policy) throws IOException {
        int futureYear = LocalDate.now().getYear() + 1;
        List<Policy> futureModels;

        // 支持单个导出
        if (policy != null && policy.getId() != null) {
            // 只导出指定id的模型
            futureModels = getFutureYearCustomModelList(policy);
        } else {
            // 导出所有预测模型
            futureModels = getFutureYearCustomModelList(new Policy());
        }

        List<Company> companies = companyMapper.selectCompanyList(null);

        List<FutureModelExportDto> exportList = new ArrayList<>();
        for (Policy model : futureModels) {
            FutureModelExportDto dto = new FutureModelExportDto();
            dto.setCompanyName(model.getName()); // 这里其实是"模型名称"

            StringBuilder reach = new StringBuilder();
            StringBuilder almostReach = new StringBuilder();
            StringBuilder notReach = new StringBuilder();
            StringBuilder indicatorDetail = new StringBuilder();

            for (Company company : companies) {
                Map<String, String> status = getCompanyStatus(company.getId(), model, futureYear, futureYear - 1);

                if (status.containsKey("stockCompany")) {
                    if (reach.length() > 0) {
                        reach.append("、");
                    }
                    reach.append(company.getCompanyName());
                }
                if (status.containsKey("cultivateCompany")) {
                    if (almostReach.length() > 0) {
                        almostReach.append("、");
                    }
                    almostReach.append(company.getCompanyName());
                }
                if (status.containsKey("retreatCompany")) {
                    if (notReach.length() > 0) {
                        notReach.append("、");
                    }
                    notReach.append(company.getCompanyName());
                }

                // 指标详情
                List<Map<String, String>> indicators = c2cNoMatchedIndicatorV2(company.getId(), model, futureYear);
                if (!indicators.isEmpty()) {
                    indicatorDetail.append("【").append(company.getCompanyName()).append("】\n");
                    indicatorDetail.append(futureYear).append("年未达标指标：\n");
                    for (Map<String, String> indicator : indicators) {
                        indicatorDetail.append("期望：").append(indicator.get("expectIndicator"))
                                .append("，实际：").append(indicator.get("yourIndicator").equals("NULL") ? "当年数据未填写" : indicator.get("yourIndicator")).append("\n");
                    }
                    indicatorDetail.append("\n");
                }
            }

            dto.setReach(reach.toString());
            dto.setAlmostReach(almostReach.toString());
            dto.setNotReach(notReach.toString());
            dto.setIndicatorDetail(indicatorDetail.toString());

            exportList.add(dto);
        }

        String filename = URLEncoder.encode("预测模型数据", "UTF-8");
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xlsx");

        ExcelUtil<FutureModelExportDto> util = new ExcelUtil<>(FutureModelExportDto.class);
        util.exportExcel(response, exportList, "预测模型数据");
    }

    @Override
    public List<PolicyAPIDto> filterPolicyAPIDtosByYesterdayDate(List<PolicyAPIDto> row) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        System.out.println("昨天------------------------------"+yesterday);

        Date yesterdayStart = Date.from(yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date yesterdayEnd = Date.from(yesterday.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println("昨天伊始------------------------"+yesterdayStart);
        System.out.println("昨天节点------------------------"+yesterdayEnd);
        List<PolicyAPIDto> filteredRow = row.stream()
                .filter(policy -> policy.getCreatedAt() != null &&
                        policy.getCreatedAt().after(yesterdayStart) &&
                        policy.getCreatedAt().before(yesterdayEnd))
                .collect(Collectors.toList());

        return filteredRow;
    }
}
