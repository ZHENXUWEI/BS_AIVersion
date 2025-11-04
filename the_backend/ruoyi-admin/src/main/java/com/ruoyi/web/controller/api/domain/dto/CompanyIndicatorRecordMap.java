package com.ruoyi.web.controller.api.domain.dto;

import com.ruoyi.web.controller.company.domain.CompanyIndicatorDictionary;

import java.util.List;

/**
 * @author Lemon-zhu119
 * @date 2025-04-21
 */
public class CompanyIndicatorRecordMap extends CompanyIndicatorDictionary {
    /**  */
    private String threshold;
    /**  */
    private String remark;
    /**  */
    private Integer year;

    /**
     * @return
     */
    @Override
    public String toString() {
        return "CompanyIndicatorRecordMap{" +
                "name='" + getName() + '\'' +
                ", key='" + getKey() + '\'' +
                ", threshold='" +getThresholdOrRemark() + '\'' +
                ", type='" + getType() + '\'' +
                ", year=" + year +
                '}';
    }

    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public CompanyIndicatorRecordMap(String name, String key, String threshold, String remark, Integer year) {
        super.setName(name);
        super.setKey(key);
        this.threshold = threshold;
        this.remark = remark;
        this.year = year;
    }
    public CompanyIndicatorRecordMap(String name, String key, String threshold, String remark,String type, Integer year) {
        super.setName(name);
        super.setKey(key);
        super.setType(type);
        this.threshold = threshold;
        this.remark = remark;
        this.year = year;
    }
    public CompanyIndicatorRecordMap(Integer id, String name, String key, String threshold, String remark, String type, Integer year, List<String> options) {
        super.setId(id);
        super.setName(name);
        super.setKey(key);
        super.setType(type);
        this.threshold = threshold;
        this.remark = remark;
        this.year = year;
        super.setOptions(options);
    }
    public CompanyIndicatorRecordMap(Integer id, String name, String key, String threshold, String remark, String type, Integer year) {
        super.setId(id);
        super.setName(name);
        super.setKey(key);
        super.setType(type);
        this.threshold = threshold;
        this.remark = remark;
        this.year = year;
    }

    /**
     * @return
     */
    public Object getThresholdOrRemark(){
        if(this.remark!=null&&!this.remark.equals("")){
            return this.remark;
        }else {
            return this.threshold;
        }
    }

    /**
     * @return
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getType(){
        return super.getType();
    }
    public void setType(String type){
        super.setType(type);
    }

    /**
     * @param key
     * @param threshold
     * @param remark
     */
    public CompanyIndicatorRecordMap(String key, String threshold, String remark) {
        super.setKey(key);
        this.threshold = threshold;
        this.remark = remark;
    }

    /**
     * @return
     */
    public String getKey() {
        return super.getKey();
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        super.setKey(key);
    }

    /**
     * @return
     */
    public String getThreshold() {
        return threshold;
    }

    /**
     * @param threshold
     */
    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    /**
     *
     */
    public CompanyIndicatorRecordMap() {
    }

    /**
     * @param key
     * @param threshold
     */
    public CompanyIndicatorRecordMap(String key, String threshold) {
        super.setKey(key);
        this.threshold = threshold;
    }
}
