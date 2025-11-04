package com.ruoyi.web.controller.company.domain;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.List;


/**
 * 【请填写功能名称】对象 company_indicator_dictionary
 *
 * @author ruoyi
 * @date 2024-10-29
 */
public class CompanyIndicatorDictionary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 数据字典ID */
    private Integer id;

    /** 数据字典名称 */
    @Excel(name = "数据字典名称")
    private String name;

    /** $column.columnComment */
    @Excel(name = "数据字典英文名", readConverterExp = "$column.readConverterExp()")
    private String key;

    @Excel(name = "类型", readConverterExp = "$column.readConverterExp()")
    //text-输入框 single-单选 multiple-复选 date-日期
    private String type;

    /** $column.columnComment */
    @Excel(name = "状态", readConverterExp = "$column.readConverterExp()")
    private Integer status;

    @Excel(name = "数据库选项", readConverterExp = "$column.readConverterExp()")
    private String option;

    @Excel(name = "选项", readConverterExp = "$column.readConverterExp()")
    private List<String> options;

    public List<String> getOptions() {
        return options;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }   
    public void setOptions(List<String> options) {
        this.options = options;
    }
    public CompanyIndicatorDictionary() {
    }

    public CompanyIndicatorDictionary(String type) {
        this.type = type;
    }

    public CompanyIndicatorDictionary(Integer id, String name, String type, Integer status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
    }


    public CompanyIndicatorDictionary(Integer id, String name, String key, String type, Integer status) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
        this.status = status;
    }

    public CompanyIndicatorDictionary(Integer id, String name, String key, String type, Integer status, String option) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
        this.status = status;
        this.option = option;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CompanyIndicatorDictionary{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", option='" + option + '\'' +
                ", options=" + options +
                '}';
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setKey(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

}