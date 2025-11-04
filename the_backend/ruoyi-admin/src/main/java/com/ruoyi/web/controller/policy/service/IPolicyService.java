package com.ruoyi.web.controller.policy.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.ruoyi.web.controller.api.domain.dto.PolicyAPIDto;
import org.apache.ibatis.annotations.Param;

import com.ruoyi.web.controller.company.domain.Company;
import com.ruoyi.web.controller.policy.domain.Policy;
import com.ruoyi.web.controller.policy.domain.PolicyModelIndicatorRecord;
import com.ruoyi.web.controller.policy.domain.dto.PolicyModel;

public interface IPolicyService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public Policy selectPolicyById(Integer id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param policy 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<Policy> selectPolicyList(Policy policy);
    public List<Policy> selectCustomModelList(Policy policy);

    /**
     * 新增【请填写功能名称】
     *
     * @param policy 【请填写功能名称】
     * @return 结果
     */
    public int insertPolicyModelRecord(Policy policy);
    public int insertPolicyRecord(Policy policy);

    /**
     * 修改【请填写功能名称】
     *
     * @param policy 【请填写功能名称】
     * @return 结果
     */
    public int updatePolicy(Policy policy);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deletePolicyByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deletePolicyById(Integer id);

    public List<Policy> selectPolicyListById(Integer id);

    public int insertPolicyModelRecord(Integer policyId, Integer dictionaryId, String operator, Double threhold);

    public int updatePolicyFormula(Integer policyId,String formula);

    public List<PolicyModel> selectPolicyModelListByPolicyId(Integer policyId);

    public List<Company> selectMatchCompany(Integer policyId,String formula);

    public List<PolicyModelIndicatorRecord> selectPolicyModelIndicatorRecordByPolicyId(Integer id);

    public List<Company> selectMatchCompanyV2(Integer policyId, String formula);

    // 旧版匹配逻辑：每个"text"类型的数字指标都拥有简单的复杂公式(仅支持简单的整数加减乘除)
    public List<Company> selectMatchCompanyV3(Integer policyId, String formula);

    // 新版匹配逻辑：指标被分为复杂指标和简单指标，仅仅针对复杂指标拥有复杂公式(支持复杂的加减乘除)
    public List<Company> selectMatchCompanyV4(Integer policyId, String formula);

    public List<Policy> selectPolicyByClass(@Param("policyType") String policyType,
                                            @Param("subPolicyType") String subPolicyType);

    public List<Policy> selectPolicyByName(String policyName);

    public List<Policy> selectPolicyByPublishDate();

    List<Policy> selectPolicyByPublishDate1();

    List<Policy> selectPolicyByRec();

    List<Policy> selectPolicyByHotLevel();

    List<Policy> getFutureYearCustomModelList(Policy policy);

    //旧版匹配逻辑(复杂公式是简单公式的叠加)
    List<Map<String, String>> c2cNoMatchedIndicator(Integer companyId, Policy policy, Integer year);

    //新版匹配逻辑(复杂公式和简单公式相区分)
    List<Map<String, String>> c2cNoMatchedIndicatorV2(Integer companyId, Policy policy, Integer year);
    //不满足条件的指标字典ID列表
    List<Integer> getUnmatchedDictionaryIds(Integer companyId, Policy policy, Integer year);

    /**
     * 获取当前年份的自定义模型列表
     *
     * @param policy 政策对象
     * @return 当前年份的自定义模型列表
     */
    List<Policy> getCurrentYearCustomModelList(Policy policy);

    /**
     * 导出政策模型数据
     *
     * @param response 响应对象
     * @param policy 政策对象
     * @throws IOException IO异常
     */
    void export(HttpServletResponse response, Policy policy) throws IOException;

    /**
     * 获取企业在指定年份的达标状态
     *
     * @param companyId 企业ID
     * @param policy 政策对象
     * @param currentYear 当前年份
     * @param lastYear 上一年份
     * @return 企业达标状态信息
     */
    Map<String, String> getCompanyStatus(Integer companyId, Policy policy, Integer currentYear, Integer lastYear);

    /**
     * 导出预测模型数据
     *
     * @param response 响应对象
     * @param policy 政策对象
     * @throws IOException IO异常
     */
    void exportFutureModel(HttpServletResponse response, Policy policy) throws IOException;

    List<PolicyAPIDto> filterPolicyAPIDtosByYesterdayDate(List<PolicyAPIDto> row);
}
