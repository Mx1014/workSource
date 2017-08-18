package com.everhomes.rest.customer;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>organizationId: 客户企业账号id</li>
 *     <li>customerNumber: 客户编码</li>
 *     <li>name: 客户名称</li>
 *     <li>nickName: 客户昵称</li>
 *     <li>categoryItemId: 客户类型id</li>
 *     <li>categoryItemName: 客户类型名</li>
 *     <li>levelItemId: 客户级别id</li>
 *     <li>levelItemName: 客户级别名</li>
 *     <li>sourceItemId: 来源途径id</li>
 *     <li>sourceItemName: 来源途径名</li>
 *     <li>contactAvatarUri: 联系人头像</li>
 *     <li>contactAvatarUrl: 联系人头像url</li>
 *     <li>contactName: 联系人名称</li>
 *     <li>contactGenderItemId: 联系人性别id</li>
 *     <li>contactGenderItemName: 联系人性别名</li>
 *     <li>contactMobile: 联系人手机号码</li>
 *     <li>contactPhone: 联系人座机号码</li>
 *     <li>contactOffficePhone: 办公电话</li>
 *     <li>contactFamilyPhone: 家庭电话</li>
 *     <li>contactEmail: 电子邮件</li>
 *     <li>contactFax: 传真</li>
 *     <li>contactAddressId: 地址id</li>
 *     <li>contactAddress: 地址</li>
 *     <li>corpEmail: 企业邮箱</li>
 *     <li>corpWebsite: 企业网址</li>
 *     <li>corpRegAddress: 企业注册地址</li>
 *     <li>corpOpAddress: 企业运营地址</li>
 *     <li>corpLegalPerson: 法人代表</li>
 *     <li>corpRegCapital: 注册资金(万元)</li>
 *     <li>corpNatureItemId: 企业性质id</li>
 *     <li>corpNatureItemName: 企业性质</li>
 *     <li>corpScale: 企业规模</li>
 *     <li>corpIndustryItemId: 行业类型id</li>
 *     <li>corpIndustryItemName: 行业类型</li>
 *     <li>corpPurposeItemId: 企业定位id</li>
 *     <li>corpPurposeItemName: 企业定位</li>
 *     <li>corpAnnualTurnover: 年营业额（万元）</li>
 *     <li>corpBusinessScope: 营业范围</li>
 *     <li>corpBusinessLicense: 营业执照号</li>
 *     <li>corpSiteArea: 场地面积</li>
 *     <li>corpEntryDate: 入住园区日期</li>
 *     <li>corpProductCategoryItemId: 产品类型id</li>
 *     <li>corpProductCategoryItemName: 产品类型</li>
 *     <li>corpProductDesc: 主要技术及产品</li>
 *     <li>corpQualificationItemId: 企业资质认证id</li>
 *     <li>corpQualificationItemName: 企业资质认证</li>
 *     <li>corpLogoUri: 企业LOGO</li>
 *     <li>corpLogoUrl: 企业LOGO url</li>
 *     <li>corpDescription: 企业简介</li>
 *     <li>corpEmployeeAmount: 员工总数</li>
 *     <li>corpEmployeeAmountMale: 男员工总数</li>
 *     <li>corpEmployeeAmountFemale: 女员工总数</li>
 *     <li>corpEmployeeAmountRd: 研发员工总数</li>
 *     <li>corpEmployeeReturneeRate: 海归人数占比(%)</li>
 *     <li>corpEmployeeAverageAge: 员工平均年龄</li>
 *     <li>corpManagerAverageAge: 高管平均年龄</li>
 *     <li>managerName: 总经理名称</li>
 *     <li>managerPhone: 总经理电话</li>
 *     <li>managerEmail: 总经理邮箱</li>
 *     <li>remark: 备注</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class EnterpriseCustomerDTO {
    private Long id;
    private Long organizationId;
    private String customerNumber;
    private String name;
    private String nickName;
    private Long categoryItemId;
    private String categoryItemName;
    private Long levelItemId;
    private String levelItemName;
    private Long sourceItemId;
    private String sourceItemName;
    private String contactAvatarUri;
    private String contactAvatarUrl;
    private String contactName;
    private Long contactGenderItemId;
    private String contactGenderItemName;
    private String contactMobile;
    private String contactPhone;
    private String contactOffficePhone;
    private String contactFamilyPhone;
    private String contactEmail;
    private String contactFax;
    private Long contactAddressId;
    private String contactAddress;
    private String corpEmail;
    private String corpWebsite;
    private String corpRegAddress;
    private String corpOpAddress;
    private String corpLegalPerson;
    private BigDecimal corpRegCapital;
    private Long corpNatureItemId;
    private String corpNatureItemName;
    private BigDecimal corpScale;
    private Long corpIndustryItemId;
    private String corpIndustryItemName;
    private Long corpPurposeItemId;
    private String corpPurposeItemName;
    private BigDecimal corpAnnualTurnover;
    private String corpBusinessScope;
    private String corpBusinessLicense;
    private BigDecimal corpSiteArea;
    private Date corpEntryDate;
    private Long corpProductCategoryItemId;
    private String corpProductCategoryItemName;
    private String corpProductDesc;
    private Long corpQualificationItemId;
    private String corpQualificationItemName;
    private String corpLogoUri;
    private String corpLogoUrl;
    private String corpDescription;
    private Integer corpEmployeeAmount;
    private Integer corpEmployeeAmountMale;
    private Integer corpEmployeeAmountFemale;
    private Integer corpEmployeeAmountRd;
    private Double corpEmployeeReturneeRate;
    private Double corpEmployeeAverageAge;
    private Double corpManagerAverageAge;
    private String managerName;
    private String managerPhone;
    private String managerEmail;
    private String remark;

    public String getContactAvatarUrl() {
        return contactAvatarUrl;
    }

    public void setContactAvatarUrl(String contactAvatarUrl) {
        this.contactAvatarUrl = contactAvatarUrl;
    }

    public String getCorpLogoUrl() {
        return corpLogoUrl;
    }

    public void setCorpLogoUrl(String corpLogoUrl) {
        this.corpLogoUrl = corpLogoUrl;
    }

    public Long getCategoryItemId() {
        return categoryItemId;
    }

    public void setCategoryItemId(Long categoryItemId) {
        this.categoryItemId = categoryItemId;
    }

    public String getCategoryItemName() {
        return categoryItemName;
    }

    public void setCategoryItemName(String categoryItemName) {
        this.categoryItemName = categoryItemName;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public Long getContactAddressId() {
        return contactAddressId;
    }

    public void setContactAddressId(Long contactAddressId) {
        this.contactAddressId = contactAddressId;
    }

    public String getContactAvatarUri() {
        return contactAvatarUri;
    }

    public void setContactAvatarUri(String contactAvatarUri) {
        this.contactAvatarUri = contactAvatarUri;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactFamilyPhone() {
        return contactFamilyPhone;
    }

    public void setContactFamilyPhone(String contactFamilyPhone) {
        this.contactFamilyPhone = contactFamilyPhone;
    }

    public String getContactFax() {
        return contactFax;
    }

    public void setContactFax(String contactFax) {
        this.contactFax = contactFax;
    }

    public Long getContactGenderItemId() {
        return contactGenderItemId;
    }

    public void setContactGenderItemId(Long contactGenderItemId) {
        this.contactGenderItemId = contactGenderItemId;
    }

    public String getContactGenderItemName() {
        return contactGenderItemName;
    }

    public void setContactGenderItemName(String contactGenderItemName) {
        this.contactGenderItemName = contactGenderItemName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactOffficePhone() {
        return contactOffficePhone;
    }

    public void setContactOffficePhone(String contactOffficePhone) {
        this.contactOffficePhone = contactOffficePhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public BigDecimal getCorpAnnualTurnover() {
        return corpAnnualTurnover;
    }

    public void setCorpAnnualTurnover(BigDecimal corpAnnualTurnover) {
        this.corpAnnualTurnover = corpAnnualTurnover;
    }

    public String getCorpBusinessLicense() {
        return corpBusinessLicense;
    }

    public void setCorpBusinessLicense(String corpBusinessLicense) {
        this.corpBusinessLicense = corpBusinessLicense;
    }

    public String getCorpBusinessScope() {
        return corpBusinessScope;
    }

    public void setCorpBusinessScope(String corpBusinessScope) {
        this.corpBusinessScope = corpBusinessScope;
    }

    public String getCorpDescription() {
        return corpDescription;
    }

    public void setCorpDescription(String corpDescription) {
        this.corpDescription = corpDescription;
    }

    public String getCorpEmail() {
        return corpEmail;
    }

    public void setCorpEmail(String corpEmail) {
        this.corpEmail = corpEmail;
    }

    public Integer getCorpEmployeeAmount() {
        return corpEmployeeAmount;
    }

    public void setCorpEmployeeAmount(Integer corpEmployeeAmount) {
        this.corpEmployeeAmount = corpEmployeeAmount;
    }

    public Integer getCorpEmployeeAmountFemale() {
        return corpEmployeeAmountFemale;
    }

    public void setCorpEmployeeAmountFemale(Integer corpEmployeeAmountFemale) {
        this.corpEmployeeAmountFemale = corpEmployeeAmountFemale;
    }

    public Integer getCorpEmployeeAmountMale() {
        return corpEmployeeAmountMale;
    }

    public void setCorpEmployeeAmountMale(Integer corpEmployeeAmountMale) {
        this.corpEmployeeAmountMale = corpEmployeeAmountMale;
    }

    public Integer getCorpEmployeeAmountRd() {
        return corpEmployeeAmountRd;
    }

    public void setCorpEmployeeAmountRd(Integer corpEmployeeAmountRd) {
        this.corpEmployeeAmountRd = corpEmployeeAmountRd;
    }


    public Date getCorpEntryDate() {
        return corpEntryDate;
    }

    public void setCorpEntryDate(Date corpEntryDate) {
        this.corpEntryDate = corpEntryDate;
    }

    public Long getCorpIndustryItemId() {
        return corpIndustryItemId;
    }

    public void setCorpIndustryItemId(Long corpIndustryItemId) {
        this.corpIndustryItemId = corpIndustryItemId;
    }

    public String getCorpIndustryItemName() {
        return corpIndustryItemName;
    }

    public void setCorpIndustryItemName(String corpIndustryItemName) {
        this.corpIndustryItemName = corpIndustryItemName;
    }

    public String getCorpLegalPerson() {
        return corpLegalPerson;
    }

    public void setCorpLegalPerson(String corpLegalPerson) {
        this.corpLegalPerson = corpLegalPerson;
    }

    public String getCorpLogoUri() {
        return corpLogoUri;
    }

    public void setCorpLogoUri(String corpLogoUri) {
        this.corpLogoUri = corpLogoUri;
    }

    public Double getCorpEmployeeAverageAge() {
        return corpEmployeeAverageAge;
    }

    public void setCorpEmployeeAverageAge(Double corpEmployeeAverageAge) {
        this.corpEmployeeAverageAge = corpEmployeeAverageAge;
    }

    public Double getCorpEmployeeReturneeRate() {
        return corpEmployeeReturneeRate;
    }

    public void setCorpEmployeeReturneeRate(Double corpEmployeeReturneeRate) {
        this.corpEmployeeReturneeRate = corpEmployeeReturneeRate;
    }

    public Double getCorpManagerAverageAge() {
        return corpManagerAverageAge;
    }

    public void setCorpManagerAverageAge(Double corpManagerAverageAge) {
        this.corpManagerAverageAge = corpManagerAverageAge;
    }

    public void setCorpRegCapital(BigDecimal corpRegCapital) {
        this.corpRegCapital = corpRegCapital;
    }

    public Long getCorpNatureItemId() {
        return corpNatureItemId;
    }

    public void setCorpNatureItemId(Long corpNatureItemId) {
        this.corpNatureItemId = corpNatureItemId;
    }

    public String getCorpNatureItemName() {
        return corpNatureItemName;
    }

    public void setCorpNatureItemName(String corpNatureItemName) {
        this.corpNatureItemName = corpNatureItemName;
    }

    public String getCorpOpAddress() {
        return corpOpAddress;
    }

    public void setCorpOpAddress(String corpOpAddress) {
        this.corpOpAddress = corpOpAddress;
    }

    public Long getCorpProductCategoryItemId() {
        return corpProductCategoryItemId;
    }

    public void setCorpProductCategoryItemId(Long corpProductCategoryItemId) {
        this.corpProductCategoryItemId = corpProductCategoryItemId;
    }

    public String getCorpProductCategoryItemName() {
        return corpProductCategoryItemName;
    }

    public void setCorpProductCategoryItemName(String corpProductCategoryItemName) {
        this.corpProductCategoryItemName = corpProductCategoryItemName;
    }

    public String getCorpProductDesc() {
        return corpProductDesc;
    }

    public void setCorpProductDesc(String corpProductDesc) {
        this.corpProductDesc = corpProductDesc;
    }

    public Long getCorpPurposeItemId() {
        return corpPurposeItemId;
    }

    public void setCorpPurposeItemId(Long corpPurposeItemId) {
        this.corpPurposeItemId = corpPurposeItemId;
    }

    public String getCorpPurposeItemName() {
        return corpPurposeItemName;
    }

    public void setCorpPurposeItemName(String corpPurposeItemName) {
        this.corpPurposeItemName = corpPurposeItemName;
    }

    public Long getCorpQualificationItemId() {
        return corpQualificationItemId;
    }

    public void setCorpQualificationItemId(Long corpQualificationItemId) {
        this.corpQualificationItemId = corpQualificationItemId;
    }

    public String getCorpQualificationItemName() {
        return corpQualificationItemName;
    }

    public void setCorpQualificationItemName(String corpQualificationItemName) {
        this.corpQualificationItemName = corpQualificationItemName;
    }

    public BigDecimal getCorpRegCapital() {
        return corpRegCapital;
    }

    public String getCorpRegAddress() {
        return corpRegAddress;
    }

    public void setCorpRegAddress(String corpRegAddress) {
        this.corpRegAddress = corpRegAddress;
    }

    public BigDecimal getCorpScale() {
        return corpScale;
    }

    public void setCorpScale(BigDecimal corpScale) {
        this.corpScale = corpScale;
    }

    public BigDecimal getCorpSiteArea() {
        return corpSiteArea;
    }

    public void setCorpSiteArea(BigDecimal corpSiteArea) {
        this.corpSiteArea = corpSiteArea;
    }

    public String getCorpWebsite() {
        return corpWebsite;
    }

    public void setCorpWebsite(String corpWebsite) {
        this.corpWebsite = corpWebsite;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLevelItemId() {
        return levelItemId;
    }

    public void setLevelItemId(Long levelItemId) {
        this.levelItemId = levelItemId;
    }

    public String getLevelItemName() {
        return levelItemName;
    }

    public void setLevelItemName(String levelItemName) {
        this.levelItemName = levelItemName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getSourceItemId() {
        return sourceItemId;
    }

    public void setSourceItemId(Long sourceItemId) {
        this.sourceItemId = sourceItemId;
    }

    public String getSourceItemName() {
        return sourceItemName;
    }

    public void setSourceItemName(String sourceItemName) {
        this.sourceItemName = sourceItemName;
    }
}
