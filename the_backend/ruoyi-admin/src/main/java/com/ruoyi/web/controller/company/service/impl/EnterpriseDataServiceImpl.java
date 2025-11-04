package com.ruoyi.web.controller.company.service.impl;

import com.ruoyi.web.controller.api.common.Result;
import com.ruoyi.web.controller.api.controller.EnterpriseController.IndicatorResult;
import com.ruoyi.web.controller.api.domain.dto.CompanyIndicatorRecordMap;
import com.ruoyi.web.controller.company.domain.Company;
import com.ruoyi.web.controller.company.domain.CompanyIndicatorDictionary;
import com.ruoyi.web.controller.company.domain.CompanyIndicatorRecord;
import com.ruoyi.web.controller.company.service.ICompanyIndicatorDictionaryService;
import com.ruoyi.web.controller.company.service.ICompanyIndicatorRecordService;
import com.ruoyi.web.controller.company.service.ICompanyService;
import com.ruoyi.web.controller.company.service.IEnterpriseDataService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.ibatis.ognl.OgnlOps.convertValue;

@Service
public class EnterpriseDataServiceImpl implements IEnterpriseDataService {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private ICompanyIndicatorRecordService companyIndicatorRecordService;

    @Autowired
    private ICompanyIndicatorDictionaryService companyIndicatorDictionaryService;

    //
    @Override
    public Result processEnterpriseData(List<IndicatorResult> indicatorResults)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<String> record = new ArrayList<>();
        Company company = new Company();
        Integer year = null;
        Integer companyId = null;

        // 处理特殊字段
        ProcessResult processResult = processSpecialFields(indicatorResults, company);
        year = processResult.getYear();
        companyId = processResult.getCompanyId();

        // 验证必要字段
        if (companyId == null) {
            return Result.error("公司id不能为空");
        }
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        System.out.println("公司id传出来是这个------------------------------------");
        System.out.println(companyId);
        company.setId(companyId);
        // 处理公司基础信息和指标信息
        processCompanyAndIndicatorData(indicatorResults, company, year, record);

        // 保存公司信息
        saveCompanyInfo(company, record);

        return Result.success(record);
    }

    private ProcessResult processSpecialFields(List<IndicatorResult> indicatorResults, Company company) {
        Integer year = null;
        Integer companyId = null;

        for (IndicatorResult result : indicatorResults) {
            if (result.getYear() != null || "id".equals(result.getKey())) {
                if (result.getYear() != null) {
                    year = result.getYear();
                }
                if ("id".equals(result.getKey())) {
                    companyId = Integer.parseInt(result.getValue());
                }
            }
        }
        return new ProcessResult(year, companyId);
    }

    private void processCompanyAndIndicatorData(List<IndicatorResult> indicatorResults, Company company, Integer year, List<String> record)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        for (IndicatorResult result : indicatorResults) {
            if (result.getKey() == null || result.getValue() == null) {
                continue;
            }
            processCompanyBasicInfo(result, company, record);
            processIndicatorInfo(result, company, year, record);
        }
    }

    private void processCompanyBasicInfo(IndicatorResult result, Company company, List<String> record)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(Company.class).getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getName().equals(result.getKey())) {
                try {
                    processCompanyProperty(result, company, propertyDescriptor, record);
                } catch (Exception e) {
                    System.out.println("设置 " + result.getKey() + " 时出现错误：" + e.getMessage());
                }
                break;
            }
        }
    }

    private void processCompanyProperty(IndicatorResult result, Company company, PropertyDescriptor propertyDescriptor, List<String> record)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class<?> propertyType = propertyDescriptor.getPropertyType();
        Object convertedValue = null;

        if ("preAddress".equals(result.getKey()) && result.getValue() != null) {
            String addressStr = result.getValue().replaceAll("[\\[\\]]", "");
            List<String> addressList = Arrays.asList(addressStr.split(",\\s*"));
            convertedValue = addressList;
        } else {
            convertedValue = convertValue(result.getValue(), propertyType);
        }

        PropertyUtils.setProperty(company, result.getKey(), convertedValue);
        record.add("设置" + result.getKey() + "=" + result.getValue());

    }

    private void processIndicatorInfo(IndicatorResult result, Company company, Integer year, List<String> record) {
        System.out.println("以下是公司信息----------------------------------------------");
        System.out.println(company);
        Object dictionaryId = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryByKey(result.getKey());
        if (dictionaryId != null) {
            if ("text".equals(result.getType())) {
                CompanyIndicatorRecord companyIndicatorRecord = new CompanyIndicatorRecord(
                        null,
                        company.getId(),
                        null,
                        (Integer) dictionaryId,
                        null,
                        null,
                        null,
                        year
                );

                // 检查value是否为数字（包括整数和小数）
                if (result.getValue() != null && result.getValue().matches("^-?\\d+(\\.\\d+)?$")) {
                    try {
                        companyIndicatorRecord.setThreshold(Double.parseDouble(result.getValue()));
                    } catch (NumberFormatException e) {
                        System.out.println("转换数值失败：" + result.getKey() + "=" + result.getValue());
                        return;
                    }
                    saveOrUpdateIndicatorRecord(companyIndicatorRecord, record, result);
                } else {
                    companyIndicatorRecord.setRemark(result.getValue());
                    saveOrUpdateIndicatorRecord(companyIndicatorRecord, record, result);
                }
            } else if ("multiple".equals(result.getType()) || "single".equals(result.getType())) {
                String value = result.getValue();
                if (value != null && !value.endsWith(";")) {
                    value = value + ";";
                }
                CompanyIndicatorRecord indicatorRecord = new CompanyIndicatorRecord(
                        null,
                        company.getId(),
                        null,
                        (Integer) dictionaryId,
                        null,
                        null,
                        null,
                        year
                );
                indicatorRecord.setRemark(value);
                saveOrUpdateIndicatorRecord(indicatorRecord, record, result);
            } else {
                CompanyIndicatorRecord indicatorRecord = new CompanyIndicatorRecord(
                        null,
                        company.getId(),
                        null,
                        (Integer) dictionaryId,
                        null,
                        null,
                        null,
                        year
                );
                indicatorRecord.setRemark(result.getValue());
                saveOrUpdateIndicatorRecord(indicatorRecord, record, result);
            }
        }
    }

    private void saveOrUpdateIndicatorRecord(CompanyIndicatorRecord companyIndicatorRecord, List<String> record, IndicatorResult result) {
        System.out.println("这个id是-----------------------------------");
        System.out.println(companyIndicatorRecord);
        CompanyIndicatorRecord companyIndicatorRecordDB = new CompanyIndicatorRecord(
                companyIndicatorRecord.getId(),
                companyIndicatorRecord.getCompanyId(),
                1,
                companyIndicatorRecord.getDictionaryId(),
                null,
                null,
                null,
                companyIndicatorRecord.getYear()
        );
        List<CompanyIndicatorRecord> existingRecords = companyIndicatorRecordService.selectCompanyIndicatorRecordList(companyIndicatorRecordDB);

        if (existingRecords.isEmpty()) {
            int insert = companyIndicatorRecordService.insertCompanyIndicatorRecord(companyIndicatorRecord);
            if (insert == 1) {
                record.add("插入" + result.getKey() + "为" + result.getValue());
            }
        } else {
            companyIndicatorRecord.setId(existingRecords.get(0).getId());
            int update = companyIndicatorRecordService.updateCompanyIndicatorRecord(companyIndicatorRecord);
            if (update == 1) {
                record.add("更新" + result.getKey() + "为" + result.getValue());
            }
        }
    }

    private void saveCompanyInfo(Company company, List<String> record) {
        if (company.getId() != null) {
            if (companyService.selectCompanyList(new Company(company.getId())).isEmpty()) {
                int insert = companyService.insertCompany(company);
                if (insert == 1) {
                    record.add("基础信息插入成功");
                }
            } else {
                int update = companyService.updateCompany(company);
                if (update == 1) {
                    record.add("基础信息更新成功");
                }
            }
        }
    }

    @Override
    public List<CompanyIndicatorRecordMap> getEnterpriseData(Integer companyId, Integer year)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        //获取附加指标(年份+公司id)
        List<CompanyIndicatorRecordMap> companyIndicatorRecordMaps = companyIndicatorRecordService
                .selectCompanyIndicatorRecordListByCompanyIdAndYear(companyId, year);
        List<CompanyIndicatorRecordMap> companyIndicatorRecordLoopMaps = new ArrayList<>();

        if (year == null) {
            year = LocalDate.now().getYear();
        }

        // 获取当前年份
        Integer localYear = LocalDate.now().getYear();

        // 获取公司信息
//        Company company = companyService.selectCompanyListById(companyId).get(0);
        List<Company> companies = companyService.selectCompanyListById(companyId);
        if (companies.isEmpty()) {
            return new ArrayList<>();
        }

        Company company = companies.get(0);
        // 定义字典列表
        List<CompanyIndicatorRecordMap> dictionary = new ArrayList<>();
        // 获取所有字典
        getAllDic(dictionary, year);

        // 获取所有指标
        // 默认补全往年或未来指标
        defaultFill(companyId, year, localYear, companyIndicatorRecordMaps, companyIndicatorRecordLoopMaps);

        companyIndicatorRecordMaps = companyIndicatorRecordService.selectCompanyIndicatorRecordListByCompanyIdAndYear(companyId, year);

        // 获取公司指标
        List<CompanyIndicatorRecordMap> companyDictionary = companyIndicatorRecordService.getCompanyDictionary(company, year);

        // 根据字典和指标进行匹配
        match(companyIndicatorRecordMaps, dictionary, companyDictionary, year);

        return companyDictionary;
    }

    private void getAllDic(List<CompanyIndicatorRecordMap> dictionary, Integer year) {
        List<CompanyIndicatorDictionary> dictionaries = companyIndicatorDictionaryService
                .selectCompanyIndicatorDictionaryList(new CompanyIndicatorDictionary());
        for (CompanyIndicatorDictionary dictionary1 : dictionaries) {
            CompanyIndicatorRecordMap companyIndicatorRecordMap = new CompanyIndicatorRecordMap(
                    dictionary1.getId(),
                    dictionary1.getName(),
                    dictionary1.getKey(),
                    null,
                    null,
                    dictionary1.getType(),
                    year,
                    dictionary1.getOptions()
            );
            dictionary.add(companyIndicatorRecordMap);
        }
    }

    private void defaultFill(Integer id, Integer year, Integer localYear, List<CompanyIndicatorRecordMap>
            companyIndicatorRecordMaps, List<CompanyIndicatorRecordMap> companyIndicatorRecordLoopMaps) {
        if (companyIndicatorRecordMaps.isEmpty()) {
            int loopYear = year;
            //如果查询年份>当前年份
            if (year > localYear) {
                //从查询年份一直找到2020年
                for (; loopYear >= localYear - 5; loopYear--) {
                    companyIndicatorRecordLoopMaps = companyIndicatorRecordService.selectCompanyIndicatorRecordListByCompanyIdAndYear(id, loopYear);
                    if (!companyIndicatorRecordLoopMaps.isEmpty()) {
                        break;
                    }
                }
                if (loopYear >= localYear - 5) {
                    loopYear++;
                    for (; loopYear <= year; loopYear++) {
                        for (CompanyIndicatorRecordMap companyIndicatorRecordMap : companyIndicatorRecordLoopMaps) {
                            Object dictionaryId = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryByKey(companyIndicatorRecordMap.getKey());
                            CompanyIndicatorRecord companyIndicatorRecord = companyIndicatorRecordService.selectCompanyIndicatorRecordList(
                                    new CompanyIndicatorRecord(null, id, null, (Integer) dictionaryId, null, null, null, loopYear - 1)
                            ).get(0);
                            companyIndicatorRecord.setYear(loopYear);
                            companyIndicatorRecordService.insertCompanyIndicatorRecord(companyIndicatorRecord);
                        }
                    }
                }
            } else if (year < localYear) {
                for (; loopYear <= localYear + 5; loopYear++) {
                    companyIndicatorRecordLoopMaps = companyIndicatorRecordService.selectCompanyIndicatorRecordListByCompanyIdAndYear(id, loopYear);
                    if (!companyIndicatorRecordLoopMaps.isEmpty()) {
                        break;
                    }
                }
                if (loopYear <= localYear + 5) {
                    loopYear--;
                    for (; loopYear >= year; loopYear--) {
                        for (CompanyIndicatorRecordMap companyIndicatorRecordMap : companyIndicatorRecordLoopMaps) {
                            Object dictionaryId = companyIndicatorDictionaryService.selectCompanyIndicatorDictionaryByKey(companyIndicatorRecordMap.getKey());
                            CompanyIndicatorRecord companyIndicatorRecord = companyIndicatorRecordService.selectCompanyIndicatorRecordList(
                                    new CompanyIndicatorRecord(null, id, null, (Integer) dictionaryId, null, null, null, loopYear + 1)
                            ).get(0);
                            companyIndicatorRecord.setYear(loopYear);
                            companyIndicatorRecordService.insertCompanyIndicatorRecord(companyIndicatorRecord);
                        }
                    }
                }
            }
        }
    }

    private void match(List<CompanyIndicatorRecordMap> companyIndicatorRecordMaps, List<CompanyIndicatorRecordMap> dictionary
            , List<CompanyIndicatorRecordMap> companyDictionary, Integer year) {
        int i = 0;
        outerLoop:
        //遍历所有字典
        for (CompanyIndicatorRecordMap companyIndicatorRecordMap : dictionary) {
            i++;
            //优化 如果字典的key不在记录的key中，则跳过直接add
            if (!companyIndicatorRecordMaps.stream()
                    .anyMatch(companyIndicatorRecordMap1 ->
                            companyIndicatorRecordMap1.getKey() != null &&
                                    companyIndicatorRecordMap1.getKey().equals(companyIndicatorRecordMap.getKey()))) {
                // 查找是否存在相同key的数据
                boolean found = false;
                for (int j = 0; j < companyDictionary.size(); j++) {
                    if (companyDictionary.get(j).getKey() != null && companyDictionary.get(j).getKey().equals(companyIndicatorRecordMap.getKey())) {
                        // 找到相同key的数据，进行更新
                        companyDictionary.set(j, new CompanyIndicatorRecordMap(
                                companyIndicatorRecordMap.getId(),
                                companyIndicatorRecordMap.getName(),
                                companyIndicatorRecordMap.getKey(),
                                null,
                                null,
                                companyIndicatorRecordMap.getType(),
                                year,
                                companyIndicatorRecordMap.getOptions()
                        ));
                        found = true;
                        break;
                    }
                }

                // 如果没有找到相同key的数据，则添加新数据
                if (!found) {
                    companyDictionary.add(new CompanyIndicatorRecordMap(
                            companyIndicatorRecordMap.getId(),
                            companyIndicatorRecordMap.getName(),
                            companyIndicatorRecordMap.getKey(),
                            null,
                            null,
                            companyIndicatorRecordMap.getType(),
                            year,
                            companyIndicatorRecordMap.getOptions()
                    ));
                }
                continue;
            }
            //遍历所有指标
            for (CompanyIndicatorRecordMap companyIndicatorRecordMap1 : companyIndicatorRecordMaps) {
                if(companyIndicatorRecordMap.getKey()==null||companyIndicatorRecordMap1.getKey()==null){
                    continue ;
                }
                //如果字典和指标匹配
                if (companyIndicatorRecordMap.getKey().equals(companyIndicatorRecordMap1.getKey())) {
                    List<String> in = new ArrayList<>();
                    in.add("hangYe_xiShu");
                    in.add("product_domain_metrics");
                    in.add("leading_product_market_share");

                    // 查找是否存在相同key的数据
                    boolean found = false;
                    for (int j = 0; j < companyDictionary.size(); j++) {
                        if(companyDictionary.get(j).getKey()==null||companyIndicatorRecordMap1.getKey()==null){
                            continue ;
                        }
                        if (companyDictionary.get(j).getKey().equals(companyIndicatorRecordMap1.getKey())) {
                            // 找到相同key的数据，进行更新
                            if (in.contains(companyIndicatorRecordMap1.getKey())) {
                                Double threshold = Double.parseDouble(companyIndicatorRecordMap1.getThreshold() == null ? "0" : companyIndicatorRecordMap1.getThreshold());
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                String formattedThreshold = decimalFormat.format(threshold);
                                companyDictionary.set(j, new CompanyIndicatorRecordMap(
                                        companyIndicatorRecordMap1.getId(),
                                        companyIndicatorRecordMap1.getName(),
                                        companyIndicatorRecordMap1.getKey(),
                                        formattedThreshold,
                                        null,
                                        companyIndicatorRecordMap1.getType(),
                                        year,
                                        companyIndicatorRecordMap1.getOptions()
                                ));
                            } else {
                                String TOR = (String) companyIndicatorRecordMap1.getThresholdOrRemark();
                                if (companyIndicatorRecordMap1.getThreshold() != null) {
                                    Double DTOR = Double.parseDouble(TOR);
                                    companyDictionary.set(j, new CompanyIndicatorRecordMap(
                                            companyIndicatorRecordMap1.getId(),
                                            companyIndicatorRecordMap1.getName(),
                                            companyIndicatorRecordMap1.getKey(),
                                            DTOR.toString(),
                                            null,
                                            companyIndicatorRecordMap1.getType(),
                                            year,
                                            companyIndicatorRecordMap1.getOptions()
                                    ));
                                } else {
                                    companyDictionary.set(j, new CompanyIndicatorRecordMap(
                                            companyIndicatorRecordMap1.getId(),
                                            companyIndicatorRecordMap1.getName(),
                                            companyIndicatorRecordMap1.getKey(),
                                            null,
                                            TOR,
                                            companyIndicatorRecordMap1.getType(),
                                            year,
                                            companyIndicatorRecordMap1.getOptions()
                                    ));
                                }
                            }
                            found = true;
                            break;
                        }
                    }

                    // 如果没有找到相同key的数据，则添加新数据
                    if (!found) {
                        if (in.contains(companyIndicatorRecordMap1.getKey())) {
                            Double threshold = Double.parseDouble(companyIndicatorRecordMap1.getThreshold() == null ? "0" : companyIndicatorRecordMap1.getThreshold());
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            String formattedThreshold = decimalFormat.format(threshold);
                            companyDictionary.add(new CompanyIndicatorRecordMap(
                                    companyIndicatorRecordMap1.getId(),
                                    companyIndicatorRecordMap1.getName(),
                                    companyIndicatorRecordMap1.getKey(),
                                    formattedThreshold,
                                    null,
                                    companyIndicatorRecordMap1.getType(),
                                    year,
                                    companyIndicatorRecordMap1.getOptions()
                            ));
                        } else {
                            String TOR = (String) companyIndicatorRecordMap1.getThresholdOrRemark();
                            if (companyIndicatorRecordMap1.getThreshold() != null) {
                                Double DTOR = Double.parseDouble(TOR);
                                companyDictionary.add(new CompanyIndicatorRecordMap(
                                        companyIndicatorRecordMap1.getId(),
                                        companyIndicatorRecordMap1.getName(),
                                        companyIndicatorRecordMap1.getKey(),
                                        DTOR.toString(),
                                        null,
                                        companyIndicatorRecordMap1.getType(),
                                        year,
                                        companyIndicatorRecordMap1.getOptions()
                                ));
                            } else {
                                companyDictionary.add(new CompanyIndicatorRecordMap(
                                        companyIndicatorRecordMap1.getId(),
                                        companyIndicatorRecordMap1.getName(),
                                        companyIndicatorRecordMap1.getKey(),
                                        null,
                                        TOR,
                                        companyIndicatorRecordMap1.getType(),
                                        year,
                                        companyIndicatorRecordMap1.getOptions()
                                ));
                            }
                        }
                    }
                    continue outerLoop;
                }
            }
        }
    }


    private static class ProcessResult {

        private Integer year;
        private Integer companyId;

        public ProcessResult(Integer year, Integer companyId) {
            this.year = year;
            this.companyId = companyId;
        }

        public Integer getYear() {
            return year;
        }

        public Integer getCompanyId() {
            return companyId;
        }
    }
}
