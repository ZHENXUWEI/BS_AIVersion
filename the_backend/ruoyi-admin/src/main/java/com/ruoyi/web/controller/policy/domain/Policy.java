package com.ruoyi.web.controller.policy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class Policy extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Integer id;

    public Policy(Integer id, Date createdAt, String name, Date publishDate, Date expireDate, String stockCompanyStatus, String cultivateCompanyStatus, String retreatCompanyStatus, String description, Integer status, String content, String formula, String phone, String contact, Integer isMoney, Integer recLevel, Integer hotLevel, String applicableArea, String policyClass, String policyType, String leaderDepartment, String money, String source, String fileUrl, Integer isCustom, Integer docNumber, String docDetail, String policyCondition, String policyDocDetail, String policyObject) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.publishDate = publishDate;
        this.expireDate = expireDate;
        this.stockCompanyStatus = stockCompanyStatus;
        this.cultivateCompanyStatus = cultivateCompanyStatus;
        this.retreatCompanyStatus = retreatCompanyStatus;
        this.description = description;
        this.status = status;
        this.content = content;
        this.formula = formula;
        this.phone = phone;
        this.contact = contact;
        this.isMoney = isMoney;
        this.recLevel = recLevel;
        this.hotLevel = hotLevel;
        this.applicableArea = applicableArea;
        this.policyClass = policyClass;
        this.policyType = policyType;
        this.leaderDepartment = leaderDepartment;
        this.money = money;
        this.source = source;
        this.fileUrl = fileUrl;
        this.isCustom = isCustom;
        this.docNumber = docNumber;
        this.docDetail = docDetail;
        this.policyCondition = policyCondition;
        this.policyDocDetail = policyDocDetail;
        this.policyObject = policyObject;
    }

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;


    /** 政策名称 */
    @Excel(name = "政策模型名称", width = 30)
    private String name;

    /** 政策发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishDate;

    /** 政策过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireDate;

    /** 存量企业状态 */
    @Excel(name = "存量企业", readConverterExp = "0=去年达标，今年未达标,1=去年达标，今年达标")
    private String stockCompanyStatus;

    /** 培育企业状态 */
    @Excel(name = "培育企业", readConverterExp = "0=去年未达标，今年未达标,1=去年未达标，今年达标")
    private String cultivateCompanyStatus;

    /** 退步企业状态 */
    @Excel(name = "退步企业", readConverterExp = "0=去年达标，今年未达标")
    private String retreatCompanyStatus;

    /** 政策申报状态 */
    @Excel(name = "申报状态", readConverterExp = "0=未达标,1=已达标")
    private String description;

    /** 状态 */
    @Excel(name = "企业状态", readConverterExp = "0=正常,1=异常")
    private Integer status;

    /** 政策内容 */
    @Excel(name = "政策内容", width = 50)
    private String content;

    /** 匹配公式 */
    @Excel(name = "匹配公式")
    private String formula;

    /** 联系电话 */
    private String phone;

    /** 联系人 */
    private String contact;

    /** 是否有资金支持 */
    private Integer isMoney;

    /** 推荐等级 */
    private Integer recLevel;

    /** 热度等级 */
    private Integer hotLevel;

    /** 适用地区 */
    @Excel(name = "适用地区")
    private String applicableArea;

    /** 政策类别 */
    @Excel(name = "政策类别")
    private String policyClass;

    /** 政策类型 */
    @Excel(name = "政策类型")
    private String policyType;

    /** 主管部门 */
    @Excel(name = "主管部门")
    private String leaderDepartment;

    /** 资金支持 */
    @Excel(name = "资金支持")
    private String money;

    /** 政策来源 */
    @Excel(name = "政策来源")
    private String source;

    private String fileUrl;
    private Integer isCustom;
    private Integer docNumber;
    
    /** 手写的材料详情 */
    private String docDetail;
    
    /** 政策条件 */
    @Excel(name = "政策条件", width = 50)
    private String policyCondition;
    
    /** 导入的材料详情 */
    private String policyDocDetail;
    
    /** 政策对象 */
    @Excel(name = "政策对象")
    private String policyObject;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", name='" + name + '\'' +
                ", publishDate=" + publishDate +
                ", expireDate=" + expireDate +
                ", stockCompanyStatus='" + stockCompanyStatus + '\'' +
                ", cultivateCompanyStatus='" + cultivateCompanyStatus + '\'' +
                ", retreatCompanyStatus='" + retreatCompanyStatus + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", content='" + content + '\'' +
                ", formula='" + formula + '\'' +
                ", phone='" + phone + '\'' +
                ", contact='" + contact + '\'' +
                ", isMoney=" + isMoney +
                ", recLevel=" + recLevel +
                ", hotLevel=" + hotLevel +
                ", applicableArea='" + applicableArea + '\'' +
                ", policyClass='" + policyClass + '\'' +
                ", policyType='" + policyType + '\'' +
                ", leaderDepartment='" + leaderDepartment + '\'' +
                ", money='" + money + '\'' +
                ", source='" + source + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", isCustom=" + isCustom +
                ", docNumber=" + docNumber +
                ", docDetail='" + docDetail + '\'' +
                ", policyCondition='" + policyCondition + '\'' +
                ", policyDocDetail='" + policyDocDetail + '\'' +
                ", policyObject='" + policyObject + '\'' +
                '}';
    }

    public String getPolicyCondition() {
        return policyCondition;
    }

    public void setPolicyCondition(String policyCondition) {
        this.policyCondition = policyCondition;
    }

    public String getPolicyDocDetail() {
        return policyDocDetail;
    }

    public void setPolicyDocDetail(String policyDocDetail) {
        this.policyDocDetail = policyDocDetail;
    }

    public String getPolicyObject() {
        return policyObject;
    }

    public void setPolicyObject(String policyObject) {
        this.policyObject = policyObject;
    }

    public Policy(Integer id, String name, Date publishDate, Date expireDate, String description, Integer status, String content, String formula, String phone, String contact, Integer isMoney, Integer recLevel, Integer hotLevel, String applicableArea, String policyClass, String policyType, String leaderDepartment, String money, String source, String fileUrl, Integer isCustom, Integer docNumber, String docDetail, String policyCondition, String policyDocDetail, String policyObject) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
        this.expireDate = expireDate;
        this.description = description;
        this.status = status;
        this.content = content;
        this.formula = formula;
        this.phone = phone;
        this.contact = contact;
        this.isMoney = isMoney;
        this.recLevel = recLevel;
        this.hotLevel = hotLevel;
        this.applicableArea = applicableArea;
        this.policyClass = policyClass;
        this.policyType = policyType;
        this.leaderDepartment = leaderDepartment;
        this.money = money;
        this.source = source;
        this.fileUrl = fileUrl;
        this.isCustom = isCustom;
        this.docNumber = docNumber;
        this.docDetail = docDetail;
        this.policyCondition = policyCondition;
        this.policyDocDetail = policyDocDetail;
        this.policyObject = policyObject;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Integer isCustom) {
        this.isCustom = isCustom;
    }

    public Integer getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(Integer docNumber) {
        this.docNumber = docNumber;
    }

    public String getDocDetail() {
        return docDetail;
    }

    public void setDocDetail(String docDetail) {
        this.docDetail = docDetail;
    }

    public Policy(Integer id, String name, Date publishDate, Date expireDate, String description, Integer status, String content, String formula, String phone, String contact, Integer isMoney, Integer recLevel, Integer hotLevel, String applicableArea, String policyClass, String policyType, String leaderDepartment, String money, String source, String fileUrl, Integer isCustom, Integer docNumber, String docDetail) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
        this.expireDate = expireDate;
        this.description = description;
        this.status = status;
        this.content = content;
        this.formula = formula;
        this.phone = phone;
        this.contact = contact;
        this.isMoney = isMoney;
        this.recLevel = recLevel;
        this.hotLevel = hotLevel;
        this.applicableArea = applicableArea;
        this.policyClass = policyClass;
        this.policyType = policyType;
        this.leaderDepartment = leaderDepartment;
        this.money = money;
        this.source = source;
        this.fileUrl = fileUrl;
        this.isCustom = isCustom;
        this.docNumber = docNumber;
        this.docDetail = docDetail;
    }

    public Policy() {
    }

    public Policy(Integer isCustom) {
        this.isCustom = isCustom;
    }

    public Policy(Integer id, String name, Date publishDate, String description, Integer status, String content, String formula) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
        this.description = description;
        this.status = status;
        this.content = content;
        this.formula = formula;
    }

    public Policy(Integer id, String name, Date publishDate, Date expireDate, String description, Integer status, String content, String formula) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
        this.expireDate = expireDate;
        this.description = description;
        this.status = status;
        this.content = content;
        this.formula = formula;
    }

    public Policy(Integer id, String name, Date publishDate, Date expireDate, String description, Integer status, String content, String formula, String phone, String contact, Integer isMoney, Integer recLevel, Integer hotLevel,String applicableArea,String policyClass,String policyType,String leaderDepartment) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
        this.expireDate = expireDate;
        this.description = description;
        this.status = status;
        this.content = content;
        this.formula = formula;
        this.phone = phone;
        this.contact = contact;
        this.isMoney = isMoney;
        this.recLevel = recLevel;
        this.hotLevel = hotLevel;
        this.applicableArea=applicableArea;
        this.policyClass=policyClass;
        this.policyType=policyType;
        this.leaderDepartment=leaderDepartment;
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

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getIsMoney() {
        return isMoney;
    }

    public void setIsMoney(Integer isMoney) {
        this.isMoney = isMoney;
    }

    public Integer getRecLevel() {
        return recLevel;
    }

    public void setRecLevel(Integer recLevel) {
        this.recLevel = recLevel;
    }

    public Integer getHotLevel() {
        return hotLevel;
    }

    public void setHotLevel(Integer hotLevel) {
        this.hotLevel = hotLevel;
    }

    public String getApplicableArea() {
        return applicableArea;
    }

    public void setApplicableArea(String applicableArea) {
        this.applicableArea = applicableArea;
    }

    public String getPolicyClass() {
        return policyClass;
    }

    public void setPolicyClass(String policyClass) {
        this.policyClass = policyClass;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public String getLeaderDepartment() {
        return leaderDepartment;
    }

    public void setLeaderDepartment(String leaderDepartment) {
        this.leaderDepartment = leaderDepartment;
    }

    public String getStockCompanyStatus() {
        return stockCompanyStatus;
    }

    public void setStockCompanyStatus(String stockCompanyStatus) {
        this.stockCompanyStatus = stockCompanyStatus;
    }

    public String getCultivateCompanyStatus() {
        return cultivateCompanyStatus;
    }

    public void setCultivateCompanyStatus(String cultivateCompanyStatus) {
        this.cultivateCompanyStatus = cultivateCompanyStatus;
    }

    public String getRetreatCompanyStatus() {
        return retreatCompanyStatus;
    }

    public void setRetreatCompanyStatus(String retreatCompanyStatus) {
        this.retreatCompanyStatus = retreatCompanyStatus;
    }
}
