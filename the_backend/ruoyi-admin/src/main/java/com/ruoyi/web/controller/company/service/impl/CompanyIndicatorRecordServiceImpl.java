package com.ruoyi.web.controller.company.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.web.controller.api.domain.dto.CompanyIndicatorRecordMap;
import com.ruoyi.web.controller.company.domain.Company;
import com.ruoyi.web.controller.company.domain.CompanyIndicatorRecord;
import com.ruoyi.web.controller.company.mapper.CompanyIndicatorRecordMapper;
import com.ruoyi.web.controller.company.service.ICompanyIndicatorDictionaryService;
import com.ruoyi.web.controller.company.service.ICompanyIndicatorRecordService;
import com.ruoyi.web.controller.company.service.ICompanyService;

@Service
public class CompanyIndicatorRecordServiceImpl implements ICompanyIndicatorRecordService
{
    @Autowired
    private CompanyIndicatorRecordMapper companyIndicatorRecordMapper;

    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ICompanyIndicatorDictionaryService companyIndicatorDictionaryService;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public CompanyIndicatorRecord selectCompanyIndicatorRecordById(Integer id)
    {
        return companyIndicatorRecordMapper.selectCompanyIndicatorRecordById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param companyIndicatorRecord 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CompanyIndicatorRecord> selectCompanyIndicatorRecordList(CompanyIndicatorRecord companyIndicatorRecord)
    {
        return companyIndicatorRecordMapper.selectCompanyIndicatorRecordList(companyIndicatorRecord);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param companyIndicatorRecord 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCompanyIndicatorRecord(CompanyIndicatorRecord companyIndicatorRecord)
    {
        return companyIndicatorRecordMapper.insertCompanyIndicatorRecord(companyIndicatorRecord);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param companyIndicatorRecord 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCompanyIndicatorRecord(CompanyIndicatorRecord companyIndicatorRecord)
    {
        return companyIndicatorRecordMapper.updateCompanyIndicatorRecord(companyIndicatorRecord);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteCompanyIndicatorRecordByIds(Integer[] ids)
    {
        return companyIndicatorRecordMapper.deleteCompanyIndicatorRecordByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteCompanyIndicatorRecordById(Integer id)
    {
        return companyIndicatorRecordMapper.deleteCompanyIndicatorRecordById(id);
    }

    @Override
    public List<CompanyIndicatorRecord> selectCompanyIndicatorRecordByDicId(Integer id) {
        return companyIndicatorRecordMapper.selectCompanyIndicatorRecordByDicId(id);
    }

    @Override
    public List<CompanyIndicatorRecordMap> selectCompanyIndicatorRecordListByCompanyId(Integer id) {
        return companyIndicatorRecordMapper.selectCompanyIndicatorRecordListByCompanyId(id);
    }

    @Override
    public int insertManageLevel(Integer companyId, Integer dictionaryId, String threshold) {
        return companyIndicatorRecordMapper.insertManageLevel(companyId,dictionaryId,threshold);
    }

    @Override
    public List<CompanyIndicatorRecordMap> selectCompanyIndicatorRecordListByCompanyIdAndYear(Integer id, Integer year) {
        List<CompanyIndicatorRecordMap> companyIndicatorRecordList = companyIndicatorRecordMapper.selectCompanyIndicatorRecordListByCompanyIdAndYear(id,year);
        for (CompanyIndicatorRecordMap companyIndicatorRecordMap : companyIndicatorRecordList) {
            if (companyIndicatorRecordMap.getOption()!=null) {
                companyIndicatorRecordMap.setOptions(Arrays.asList(companyIndicatorRecordMap.getOption().split(";")));
            }
        }
        return companyIndicatorRecordList;
    }

    @Override
    public List<CompanyIndicatorRecordMap> getCompanyDictionary(Company company,Integer year) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, Object> companyMap = PropertyUtils.describe(company);
        List<CompanyIndicatorRecordMap> companyDictionary = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Object> entry : companyMap.entrySet()) {
            i++;
            if(isBaseEntity(entry.getKey())){
                continue;
            }
            try {
                // 获取字段上的@Excel注解
                Field field = Company.class.getDeclaredField(entry.getKey());
                Excel excelAnnotation = field.getAnnotation(Excel.class);
                String name = excelAnnotation != null ? excelAnnotation.name() : entry.getKey();

                if(entry.getValue()!=null){
                    companyDictionary.add(new CompanyIndicatorRecordMap(name, entry.getKey(), entry.getValue().toString(), null,"text", year));
                }else{
                    companyDictionary.add(new CompanyIndicatorRecordMap(name, entry.getKey(), null, null,"text", year));
                }
            } catch (NoSuchFieldException e) {
                // 如果找不到字段，就使用原来的key作为name
                if(entry.getValue()!=null){
                    companyDictionary.add(new CompanyIndicatorRecordMap(entry.getKey(), entry.getKey(), entry.getValue().toString(), null,"text", year));
                }else{
                    companyDictionary.add(new CompanyIndicatorRecordMap(entry.getKey(), entry.getKey(), null, null,"text", year));
                }
            }
        }
        return companyDictionary;
    }
    //判断是否是BaseEntity的属性或者是class
    private boolean isBaseEntity(String key) {
        return key.equals("createBy") || key.equals("createTime") || key.equals("updateBy") || key.equals("updateTime") || key.equals("remark") || key.equals("params")||key.equals("searchValue")||key.equals("class")||key.equals("sysUserId")||key.equals("openid");
    }

}