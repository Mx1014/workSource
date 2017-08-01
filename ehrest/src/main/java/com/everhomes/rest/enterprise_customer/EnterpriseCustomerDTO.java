package com.everhomes.rest.enterprise_customer;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li></li>
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
    private String corpRegCapital;
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
    private String corpDescription;
    private Integer corpEmployeeAmount;
    private Integer corpEmployeeAmountMale;
    private Integer corpEmployeeAmountFemale;
    private Integer corpEmployeeAmountRd;
    private Integer corpEmployeeReturneeRate;
    private Integer corpEmployeeAverageAge;
    private Integer corpManagerAverageAge;
    private String managerName;
    private String managerPhone;
    private String managerEmail;
    private String remark;

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

    public Integer getCorpEmployeeAverageAge() {
        return corpEmployeeAverageAge;
    }

    public void setCorpEmployeeAverageAge(Integer corpEmployeeAverageAge) {
        this.corpEmployeeAverageAge = corpEmployeeAverageAge;
    }

    public Integer getCorpEmployeeReturneeRate() {
        return corpEmployeeReturneeRate;
    }

    public void setCorpEmployeeReturneeRate(Integer corpEmployeeReturneeRate) {
        this.corpEmployeeReturneeRate = corpEmployeeReturneeRate;
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

    public Integer getCorpManagerAverageAge() {
        return corpManagerAverageAge;
    }

    public void setCorpManagerAverageAge(Integer corpManagerAverageAge) {
        this.corpManagerAverageAge = corpManagerAverageAge;
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

    public String getCorpRegAddress() {
        return corpRegAddress;
    }

    public void setCorpRegAddress(String corpRegAddress) {
        this.corpRegAddress = corpRegAddress;
    }

    public String getCorpRegCapital() {
        return corpRegCapital;
    }

    public void setCorpRegCapital(String corpRegCapital) {
        this.corpRegCapital = corpRegCapital;
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
