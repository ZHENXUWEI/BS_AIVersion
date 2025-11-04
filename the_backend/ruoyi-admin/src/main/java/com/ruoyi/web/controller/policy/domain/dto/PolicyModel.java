package com.ruoyi.web.controller.policy.domain.dto;

import java.util.List;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class PolicyModel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @Excel(name = "政策名", width = 30, align = HorizontalAlignment.LEFT)
    private String name;

    @Excel(name = "英译", width = 20)
    private String key;

    @Excel(name = "政策Id", cellType = Excel.ColumnType.NUMERIC)
    private Integer policyId;

    @Excel(name = "指标ID", cellType = Excel.ColumnType.NUMERIC)
    private Integer indicatorDictionaryId;

    @Excel(name = "操作符", width = 15)
    private String operator;

    @Excel(name = "公式", width = 30)
    private String formula;

    @Excel(name = "复杂公式", width = 30)
    private String complexFormula;

    @Excel(name = "指标类型", width = 20)
    private String type;

    @Excel(name = "指标类型(是否拥有复杂公式)", width = 20)
    private String indicatorType;

    @Excel(name = "复杂公式拆分", width = 40)
    private List<String> complexFormulaList;

    @Excel(name = "阈值", cellType = Excel.ColumnType.NUMERIC)
    private Double threshold;

    public PolicyModel(Integer id, String name, String key, Integer policyId, Integer indicatorDictionaryId, String operator, String formula, String complexFormula, String type, List<String> complexFormulaList, Double threshold) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.policyId = policyId;
        this.indicatorDictionaryId = indicatorDictionaryId;
        this.operator = operator;
        this.formula = formula;
        this.complexFormula = complexFormula;
        this.type = type;
        this.complexFormulaList = complexFormulaList;
        this.threshold = threshold;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(String indicatorType) {
        this.indicatorType = indicatorType;
    }

    public PolicyModel(Integer id, String name, String key, Integer policyId, Integer indicatorDictionaryId, String operator, String formula, String complexFormula, String type, String indicatorType, List<String> complexFormulaList, Double threshold) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.policyId = policyId;
        this.indicatorDictionaryId = indicatorDictionaryId;
        this.operator = operator;
        this.formula = formula;
        this.complexFormula = complexFormula;
        this.type = type;
        this.indicatorType = indicatorType;
        this.complexFormulaList = complexFormulaList;
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "PolicyModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", policyId=" + policyId +
                ", indicatorDictionaryId=" + indicatorDictionaryId +
                ", operator='" + operator + '\'' +
                ", formula='" + formula + '\'' +
                ", complexFormula='" + complexFormula + '\'' +
                ", type='" + type + '\'' +
                ", complexFormulaList=" + complexFormulaList +
                ", threshold=" + threshold +
                '}';
    }

    public PolicyModel(Integer id, String name, String key, Integer policyId, Integer indicatorDictionaryId, String operator, String formula, Double threshold) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.policyId = policyId;
        this.indicatorDictionaryId = indicatorDictionaryId;
        this.operator = operator;
        this.formula = formula;
        this.threshold = threshold;
    }

    public PolicyModel(Integer id, String name, String key, Integer policyId, Integer indicatorDictionaryId, String operator, String formula, String complexFormula, Double threshold) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.policyId = policyId;
        this.indicatorDictionaryId = indicatorDictionaryId;
        this.operator = operator;
        this.formula = formula;
        this.complexFormula = complexFormula;
        this.threshold = threshold;
    }

    public PolicyModel(Integer id, String name, String key, Integer policyId, Integer indicatorDictionaryId, String operator, String formula, String complexFormula, List<String> complexFormulaList, Double threshold) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.policyId = policyId;
        this.indicatorDictionaryId = indicatorDictionaryId;
        this.operator = operator;
        this.formula = formula;
        this.complexFormula = complexFormula;
        this.complexFormulaList = complexFormulaList;
        this.threshold = threshold;
    }

    public PolicyModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    public Integer getIndicatorDictionaryId() {
        return indicatorDictionaryId;
    }

    public void setIndicatorDictionaryId(Integer indicatorDictionaryId) {
        this.indicatorDictionaryId = indicatorDictionaryId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getComplexFormula() {
        return complexFormula;
    }

    public void setComplexFormula(String complexFormula) {
        this.complexFormula = complexFormula;
    }

    public List<String> getComplexFormulaList() {
        return complexFormulaList;
    }

    public void setComplexFormulaList(List<String> complexFormulaList) {
        this.complexFormulaList = complexFormulaList;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

}
