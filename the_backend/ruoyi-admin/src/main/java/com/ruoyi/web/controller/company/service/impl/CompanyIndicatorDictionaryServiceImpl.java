package com.ruoyi.web.controller.company.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.controller.company.domain.CompanyIndicatorDictionary;
import com.ruoyi.web.controller.company.mapper.CompanyIndicatorDictionaryMapper;
import com.ruoyi.web.controller.company.service.ICompanyIndicatorDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2024-10-29
 */
@Service
public class CompanyIndicatorDictionaryServiceImpl implements ICompanyIndicatorDictionaryService {
    @Autowired
    private CompanyIndicatorDictionaryMapper companyIndicatorDictionaryMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public CompanyIndicatorDictionary selectCompanyIndicatorDictionaryById(Integer id) {
        CompanyIndicatorDictionary companyIndicatorDictionary = companyIndicatorDictionaryMapper.selectCompanyIndicatorDictionaryById(id);
        List<String> types = Arrays.asList("single", "multiple");
        if (companyIndicatorDictionary != null && types.contains(companyIndicatorDictionary.getType()) && StringUtils.isNotEmpty(companyIndicatorDictionary.getOption())) {
            companyIndicatorDictionary.setOptions(Arrays.asList(companyIndicatorDictionary.getOption().split(";")));
        }
        return companyIndicatorDictionary;
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param companyIndicatorDictionary 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CompanyIndicatorDictionary> selectCompanyIndicatorDictionaryList(CompanyIndicatorDictionary companyIndicatorDictionary) {

        List<CompanyIndicatorDictionary> companyIndicatorDictionaries = companyIndicatorDictionaryMapper.selectCompanyIndicatorDictionaryList(companyIndicatorDictionary);
        for (CompanyIndicatorDictionary companyIndicatorDictionary1 : companyIndicatorDictionaries) {
            if (companyIndicatorDictionary1.getOption()!=null) {
                companyIndicatorDictionary1.setOptions(Arrays.asList(companyIndicatorDictionary1.getOption().split(";")));
            }
        }
        return companyIndicatorDictionaries;
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param companyIndicatorDictionary 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCompanyIndicatorDictionary(CompanyIndicatorDictionary companyIndicatorDictionary) {
        return companyIndicatorDictionaryMapper.insertCompanyIndicatorDictionary(companyIndicatorDictionary);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param companyIndicatorDictionary 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCompanyIndicatorDictionary(CompanyIndicatorDictionary companyIndicatorDictionary) {
        return companyIndicatorDictionaryMapper.updateCompanyIndicatorDictionary(companyIndicatorDictionary);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteCompanyIndicatorDictionaryByIds(Integer[] ids) {
        return companyIndicatorDictionaryMapper.deleteCompanyIndicatorDictionaryByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteCompanyIndicatorDictionaryById(Integer id) {
        return companyIndicatorDictionaryMapper.deleteCompanyIndicatorDictionaryById(id);
    }

    @Override
    public List<CompanyIndicatorDictionary> selectCompanyIndicatorDictionaryListDistinct(Integer id) {
        return companyIndicatorDictionaryMapper.selectCompanyIndicatorDictionaryListDistinct(id);
    }

    @Override
    public List<CompanyIndicatorDictionary> selectCompanyIndicatorDictionaryTextList() {
        List<CompanyIndicatorDictionary> companyIndicatorDictionaries = companyIndicatorDictionaryMapper.selectCompanyIndicatorDictionaryList(new CompanyIndicatorDictionary("text"));
        return companyIndicatorDictionaries;
    }

    @Override
    public CompanyIndicatorDictionary selectCompanyIndicatorDictionaryByName(String name) {
        return companyIndicatorDictionaryMapper.selectCompanyIndicatorDictionaryByName(name);
    }

    @Override
    public List<CompanyIndicatorDictionary> selectCompanyIndicatorDictionaryByCompanyId(Integer companyId, Integer year) {
        return companyIndicatorDictionaryMapper.selectCompanyIndicatorDictionaryByCompanyId(companyId, year);
    }

    @Override
    public Object selectCompanyIndicatorDictionaryByKey(String key) {
        return companyIndicatorDictionaryMapper.selectCompanyIndicatorDictionaryByKey(key);
    }

    @Override
    public CompanyIndicatorDictionary selectCompanyIndicatorDictionaryByIdV0(Integer id) {
        CompanyIndicatorDictionary companyIndicatorDictionary= companyIndicatorDictionaryMapper.selectCompanyIndicatorDictionaryById(id);
        if (StringUtils.isNotEmpty(companyIndicatorDictionary.getOption())){
            companyIndicatorDictionary.setOptions(Arrays.asList(companyIndicatorDictionary.getOption().split(";")));
        }
        return companyIndicatorDictionary;
    }
}