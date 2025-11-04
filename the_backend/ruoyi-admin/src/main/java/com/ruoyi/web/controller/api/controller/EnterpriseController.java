package com.ruoyi.web.controller.api.controller;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.web.controller.api.common.Result;
import com.ruoyi.web.controller.api.domain.dto.CompanyIndicatorRecordMap;
import com.ruoyi.web.controller.company.service.ICompanyIndicatorRecordService;
import com.ruoyi.web.controller.company.service.IEnterpriseDataService;
import com.ruoyi.web.controller.policy.domain.Policy;
import com.ruoyi.web.controller.policy.service.IPolicyService;

/**
 * @author Lemon-zhu119
 * @date 2025-04-21
 */
@RestController
@RequestMapping("/api/policy")
public class EnterpriseController {

    @Autowired
    private IPolicyService policyService;
    /**
     * 公司对应指标的Service层处理
     */
    @Autowired
    private ICompanyIndicatorRecordService companyIndicatorRecordService;

    @Autowired
    private IEnterpriseDataService enterpriseDataService;

    /**
     * @param id
     * @param year
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    @GetMapping("/enterprisedata")
    public Result<List<IndicatorResult>> getUserInfo(@RequestParam Integer id, @RequestParam(required = false) Integer year)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<CompanyIndicatorRecordMap> companyDictionary = enterpriseDataService.getEnterpriseData(id, year);
        System.out.println(companyDictionary);
        return Result.success(setIndicatorResultList(companyDictionary));
    }

    /**
     *
     * @param id
     * @param policyId
     * @param year
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    //获取企业指标对某一模型不达标的指标
    @GetMapping("/policyModelUnqualified")
    public Result<List<IndicatorResult>> getPolicyModelUnqualified(@RequestParam("id") Integer id, @RequestParam("policyId") Integer policyId, @RequestParam(required = false) Integer year) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Policy policy = policyService.selectPolicyById(policyId);
        if (policy == null) {
            return Result.error("政策不存在");
        }
        if (year == null) {
            year = Integer.parseInt(policy.getName().substring(0, 4));
        }
        List<CompanyIndicatorRecordMap> companyDictionary = enterpriseDataService.getEnterpriseData(id, year);
        //获取不满足条件的指标字典ID列表
        List<Integer> unmatchedDictionaryIds = policyService.getUnmatchedDictionaryIds(id, policy, year);
        List<CompanyIndicatorRecordMap> unmatchedCompanyDictionary = new ArrayList<>();
        if (unmatchedDictionaryIds.isEmpty()) {
            return Result.success(setIndicatorResultList(companyDictionary));
        }
        //获取不满足条件的指标字典ID列表和当前的数值匹配
        for (CompanyIndicatorRecordMap companyIndicatorRecordMap : companyDictionary) {
            //如果指标字典ID不为空，则进行匹配如果指标字典ID为空，则是企业的基础信息
            if (companyIndicatorRecordMap.getId() != null) {
                if (unmatchedDictionaryIds.contains(companyIndicatorRecordMap.getId())) {
                    unmatchedCompanyDictionary.add(companyIndicatorRecordMap);
                }
            }
        }
        return Result.success(setIndicatorResultList(unmatchedCompanyDictionary));
    }

    /**
     * 对企业指标进行更新
     *
     * @param indicatorResults
     * @return 自行封装一个返回类对请求进行统一的值返回
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    @PostMapping("/enterprisedata")
    public Result insertCompany(@RequestBody List<IndicatorResult> indicatorResults) throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return enterpriseDataService.processEnterpriseData(indicatorResults);
    }

    private List<IndicatorResult> setIndicatorResultList(List<CompanyIndicatorRecordMap> companyDictionary) {
        List<IndicatorResult> indicatorResults = new ArrayList<>();
        for (CompanyIndicatorRecordMap companyIndicatorRecordMap : companyDictionary) {
            indicatorResults.add(setIndicatorResult(companyIndicatorRecordMap));
        }
        
        // 定义类型优先级
        Map<String, Integer> typePriority = new HashMap<>();
        typePriority.put("text", 1);
        typePriority.put("date", 2);
        typePriority.put("region", 3);
        typePriority.put("single", 4);
        typePriority.put("multiple", 5);
        
        // 按类型优先级排序
        indicatorResults.sort((a, b) -> {
            int priorityA = typePriority.getOrDefault(a.getType(), 99);
            int priorityB = typePriority.getOrDefault(b.getType(), 99);
            return priorityA - priorityB;
        });
        
        return indicatorResults;
    }

    private IndicatorResult setIndicatorResult(CompanyIndicatorRecordMap companyIndicatorRecordMap) {
        IndicatorResult indicatorResult = new IndicatorResult();
        indicatorResult.setName(companyIndicatorRecordMap.getName());
        indicatorResult.setKey(companyIndicatorRecordMap.getKey());
        indicatorResult.setValue((String) companyIndicatorRecordMap.getThresholdOrRemark());
        indicatorResult.setType(companyIndicatorRecordMap.getType());
        indicatorResult.setYear(companyIndicatorRecordMap.getYear());
        indicatorResult.setOptions(companyIndicatorRecordMap.getOptions());
        return indicatorResult;
    }

    //封装返回结果
    public static class IndicatorResult {

        private String name;
        private String key;
        private String value;
        private String type;
        private Integer year;
        private List<String> options;

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }

        public IndicatorResult() {
        }

        public IndicatorResult(String name, String key, String value, String type, Integer year) {
            this.name = name;
            this.key = key;
            this.value = value;
            this.type = type;
            this.year = year;
        }

        public IndicatorResult(String name, String key, String value, String type, Integer year, List<String> options) {
            this.name = name;
            this.key = key;
            this.value = value;
            this.type = type;
            this.year = year;
            this.options = options;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "IndicatorResult{"
                    + "name='" + name + '\''
                    + ", key='" + key + '\''
                    + ", value='" + value + '\''
                    + ", type='" + type + '\''
                    + ", year=" + year + '\''
                    + ", options=" + options
                    + '}';
        }
    }

}
