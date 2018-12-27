package com.everhomes.rest.investment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.customer.CreateCustomerTrackingCommand;
import com.everhomes.rest.customer.CustomerAttachmentDTO;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class InvitedCustomerDTO {

    private Long id ;
    private Integer namespaceId;
    private Long orgId;
    private String customerNumber;
    private String name;
    private String nickName;
    private Long categoryItemId;
    private Long communityId;
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
    private Long corpEntryDate;
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
    private Double corpEmployeeReturneeRate;
    private Double corpEmployeeAverageAge;
    private Double corpManagerAverageAge;
    private String managerName;
    private String managerPhone;
    private String managerEmail;
    private String remark;

    private Long trackingUid;
    private Double propertyArea;
    private Double propertyUnitPrice;
    private Long propertyType;
    private Double longitude;
    private Double latitude;
    private String contactDuty;
    private String founderIntroduce;
    private Long foundingTime;
    private Long registrationTypeId;
    private Long technicalFieldId;
    private Long taxpayerTypeId;
    private Long relationWillingId;
    private Long highAndNewTechId;
    private Long entrepreneurialCharacteristicsId;
    private Long serialEntrepreneurId;
    private BigDecimal riskInvestmentAmount;
    private Byte  deviceType;

    private String unifiedSocialCreditCode;
    private String postUri;
    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> banner;
    private String hotline;
    //potential data primary key
    private Long sourceId;
    //service alliance activity
    private String sourceType;

    private Long originPotentialSourceId;


    //新增的客户字段
    private Long buyOrLeaseItemId;
    private String buyOrLeaseItemName;
    private String bizAddress;
    private String bizLife;
    private String customerIntentionLevel;
    private String enterDevGoal;
    private String controllerName;
    private String controllerSunBirth;
    private String controllerLunarBirth;
    private Long financingDemandItemId;
    private String financingDemandItemName;
    private String stringTag1;
    private String stringTag2;
    private String stringTag3;
    private String stringTag4;
    private String stringTag5;
    private String stringTag6;
    private String stringTag7;
    private String stringTag8;
    private String stringTag9;
    private String stringTag10;
    private String stringTag11;
    private String stringTag12;
    private String stringTag13;
    private String stringTag14;
    private String stringTag15;
    private String stringTag16;
    private Long dropBox1ItemId;
    private Long dropBox2ItemId;
    private Long dropBox3ItemId;
    private Long dropBox4ItemId;
    private Long dropBox5ItemId;
    private Long dropBox6ItemId;
    private Long dropBox7ItemId;
    private Long dropBox8ItemId;
    private Long dropBox9ItemId;

    private String dropBox1ItemName;
    private String dropBox2ItemName;
    private String dropBox3ItemName;
    private String dropBox4ItemName;
    private String dropBox5ItemName;
    private String dropBox6ItemName;
    private String dropBox7ItemName;
    private String dropBox8ItemName;
    private String dropBox9ItemName;

    private Long aptitudeFlagItemId;
    private String aptitudeFlagItemName;


    private Timestamp createTime;


    @ItemType(CustomerAttachmentDTO.class)
    private List<CustomerAttachmentDTO> attachments;

    private String transactionRatio;
    private Long expectedSignDate;
    private Byte customerSource;
    private Long entryStatusItemId;
    private String entryStatusItemName;

    // we should add new module fields
    // investment enterprise tracking infos
    @ItemType(CreateCustomerTrackingCommand.class)
    private List<CreateCustomerTrackingCommand> trackingInfos;
    @ItemType(CustomerContactDTO.class)
    private List<CustomerContactDTO>  contacts ;
    @ItemType(CustomerTrackerDTO.class)
    private List<CustomerTrackerDTO> trackers;
    private CustomerRequirementDTO requirement;
    private CustomerCurrentRentDTO currentRent;

    private Long organizationId;

    private Timestamp updateTime;

    private String legalAddress;
    private String legalAddressZip;
    private String postalAddress;
    private String postalAddressZip;
    private String taxpayerIdentificationCode;
    private String identifyCardNumber;
    private String openingBank;
    private String openingName;
    private String openingAccount;
    private String stringTag17;
    private String stringTag18;
    private String stringTag19;
    private String stringTag20;
    private String stringTag21;
    private String corpLegalPersonDuty;

    private Timestamp lastVisitingTime;


    public Timestamp getLastVisitingTime() {
        return lastVisitingTime;
    }

    public void setLastVisitingTime(Timestamp lastVisitingTime) {
        this.lastVisitingTime = lastVisitingTime;
    }

    public String getStringTag17() {
        return stringTag17;
    }

    public void setStringTag17(String stringTag17) {
        this.stringTag17 = stringTag17;
    }

    public String getStringTag18() {
        return stringTag18;
    }

    public void setStringTag18(String stringTag18) {
        this.stringTag18 = stringTag18;
    }

    public String getStringTag19() {
        return stringTag19;
    }

    public void setStringTag19(String stringTag19) {
        this.stringTag19 = stringTag19;
    }

    public String getStringTag20() {
        return stringTag20;
    }

    public void setStringTag20(String stringTag20) {
        this.stringTag20 = stringTag20;
    }

    public String getStringTag21() {
        return stringTag21;
    }

    public void setStringTag21(String stringTag21) {
        this.stringTag21 = stringTag21;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public String getLegalAddressZip() {
        return legalAddressZip;
    }

    public void setLegalAddressZip(String legalAddressZip) {
        this.legalAddressZip = legalAddressZip;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPostalAddressZip() {
        return postalAddressZip;
    }

    public void setPostalAddressZip(String postalAddressZip) {
        this.postalAddressZip = postalAddressZip;
    }

    public String getTaxpayerIdentificationCode() {
        return taxpayerIdentificationCode;
    }

    public void setTaxpayerIdentificationCode(String taxpayerIdentificationCode) {
        this.taxpayerIdentificationCode = taxpayerIdentificationCode;
    }

    public String getIdentifyCardNumber() {
        return identifyCardNumber;
    }

    public void setIdentifyCardNumber(String identifyCardNumber) {
        this.identifyCardNumber = identifyCardNumber;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public String getOpeningName() {
        return openingName;
    }

    public void setOpeningName(String openingName) {
        this.openingName = openingName;
    }

    public String getOpeningAccount() {
        return openingAccount;
    }

    public void setOpeningAccount(String openingAccount) {
        this.openingAccount = openingAccount;
    }


    public String getCorpLegalPersonDuty() {
        return corpLegalPersonDuty;
    }

    public void setCorpLegalPersonDuty(String corpLegalPersonDuty) {
        this.corpLegalPersonDuty = corpLegalPersonDuty;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getTransactionRatio() {
        return transactionRatio;
    }

    public void setTransactionRatio(String transactionRatio) {
        this.transactionRatio = transactionRatio;
    }

    public Byte getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(Byte customerSource) {
        this.customerSource = customerSource;
    }

    public List<CreateCustomerTrackingCommand> getTrackingInfos() {
        return trackingInfos;
    }

    public void setTrackingInfos(List<CreateCustomerTrackingCommand> trackingInfos) {
        this.trackingInfos = trackingInfos;
    }

    public Long getExpectedSignDate() {
        return expectedSignDate;
    }

    public void setExpectedSignDate(Long expectedSignDate) {
        this.expectedSignDate = expectedSignDate;
    }


    public Long getEntryStatusItemId() {
        return entryStatusItemId;
    }

    public void setEntryStatusItemId(Long entryStatusItemId) {
        this.entryStatusItemId = entryStatusItemId;
    }

    public String getEntryStatusItemName() {
        return entryStatusItemName;
    }

    public void setEntryStatusItemName(String entryStatusItemName) {
        this.entryStatusItemName = entryStatusItemName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
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

    public Long getCategoryItemId() {
        return categoryItemId;
    }

    public void setCategoryItemId(Long categoryItemId) {
        this.categoryItemId = categoryItemId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCategoryItemName() {
        return categoryItemName;
    }

    public void setCategoryItemName(String categoryItemName) {
        this.categoryItemName = categoryItemName;
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

    public String getContactAvatarUri() {
        return contactAvatarUri;
    }

    public void setContactAvatarUri(String contactAvatarUri) {
        this.contactAvatarUri = contactAvatarUri;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactOffficePhone() {
        return contactOffficePhone;
    }

    public void setContactOffficePhone(String contactOffficePhone) {
        this.contactOffficePhone = contactOffficePhone;
    }

    public String getContactFamilyPhone() {
        return contactFamilyPhone;
    }

    public void setContactFamilyPhone(String contactFamilyPhone) {
        this.contactFamilyPhone = contactFamilyPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactFax() {
        return contactFax;
    }

    public void setContactFax(String contactFax) {
        this.contactFax = contactFax;
    }

    public Long getContactAddressId() {
        return contactAddressId;
    }

    public void setContactAddressId(Long contactAddressId) {
        this.contactAddressId = contactAddressId;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getCorpEmail() {
        return corpEmail;
    }

    public void setCorpEmail(String corpEmail) {
        this.corpEmail = corpEmail;
    }

    public String getCorpWebsite() {
        return corpWebsite;
    }

    public void setCorpWebsite(String corpWebsite) {
        this.corpWebsite = corpWebsite;
    }

    public String getCorpRegAddress() {
        return corpRegAddress;
    }

    public void setCorpRegAddress(String corpRegAddress) {
        this.corpRegAddress = corpRegAddress;
    }

    public String getCorpOpAddress() {
        return corpOpAddress;
    }

    public void setCorpOpAddress(String corpOpAddress) {
        this.corpOpAddress = corpOpAddress;
    }

    public String getCorpLegalPerson() {
        return corpLegalPerson;
    }

    public void setCorpLegalPerson(String corpLegalPerson) {
        this.corpLegalPerson = corpLegalPerson;
    }

    public BigDecimal getCorpRegCapital() {
        return corpRegCapital;
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

    public BigDecimal getCorpScale() {
        return corpScale;
    }

    public void setCorpScale(BigDecimal corpScale) {
        this.corpScale = corpScale;
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

    public BigDecimal getCorpAnnualTurnover() {
        return corpAnnualTurnover;
    }

    public void setCorpAnnualTurnover(BigDecimal corpAnnualTurnover) {
        this.corpAnnualTurnover = corpAnnualTurnover;
    }

    public String getCorpBusinessScope() {
        return corpBusinessScope;
    }

    public void setCorpBusinessScope(String corpBusinessScope) {
        this.corpBusinessScope = corpBusinessScope;
    }

    public String getCorpBusinessLicense() {
        return corpBusinessLicense;
    }

    public void setCorpBusinessLicense(String corpBusinessLicense) {
        this.corpBusinessLicense = corpBusinessLicense;
    }

    public BigDecimal getCorpSiteArea() {
        return corpSiteArea;
    }

    public void setCorpSiteArea(BigDecimal corpSiteArea) {
        this.corpSiteArea = corpSiteArea;
    }

    public Long getCorpEntryDate() {
        return corpEntryDate;
    }

    public void setCorpEntryDate(Long corpEntryDate) {
        this.corpEntryDate = corpEntryDate;
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

    public String getCorpLogoUri() {
        return corpLogoUri;
    }

    public void setCorpLogoUri(String corpLogoUri) {
        this.corpLogoUri = corpLogoUri;
    }

    public String getCorpDescription() {
        return corpDescription;
    }

    public void setCorpDescription(String corpDescription) {
        this.corpDescription = corpDescription;
    }

    public Integer getCorpEmployeeAmount() {
        return corpEmployeeAmount;
    }

    public void setCorpEmployeeAmount(Integer corpEmployeeAmount) {
        this.corpEmployeeAmount = corpEmployeeAmount;
    }

    public Integer getCorpEmployeeAmountMale() {
        return corpEmployeeAmountMale;
    }

    public void setCorpEmployeeAmountMale(Integer corpEmployeeAmountMale) {
        this.corpEmployeeAmountMale = corpEmployeeAmountMale;
    }

    public Integer getCorpEmployeeAmountFemale() {
        return corpEmployeeAmountFemale;
    }

    public void setCorpEmployeeAmountFemale(Integer corpEmployeeAmountFemale) {
        this.corpEmployeeAmountFemale = corpEmployeeAmountFemale;
    }

    public Integer getCorpEmployeeAmountRd() {
        return corpEmployeeAmountRd;
    }

    public void setCorpEmployeeAmountRd(Integer corpEmployeeAmountRd) {
        this.corpEmployeeAmountRd = corpEmployeeAmountRd;
    }

    public Double getCorpEmployeeReturneeRate() {
        return corpEmployeeReturneeRate;
    }

    public void setCorpEmployeeReturneeRate(Double corpEmployeeReturneeRate) {
        this.corpEmployeeReturneeRate = corpEmployeeReturneeRate;
    }

    public Double getCorpEmployeeAverageAge() {
        return corpEmployeeAverageAge;
    }

    public void setCorpEmployeeAverageAge(Double corpEmployeeAverageAge) {
        this.corpEmployeeAverageAge = corpEmployeeAverageAge;
    }

    public Double getCorpManagerAverageAge() {
        return corpManagerAverageAge;
    }

    public void setCorpManagerAverageAge(Double corpManagerAverageAge) {
        this.corpManagerAverageAge = corpManagerAverageAge;
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

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTrackingUid() {
        return trackingUid;
    }

    public void setTrackingUid(Long trackingUid) {
        this.trackingUid = trackingUid;
    }

    public Double getPropertyArea() {
        return propertyArea;
    }

    public void setPropertyArea(Double propertyArea) {
        this.propertyArea = propertyArea;
    }

    public Double getPropertyUnitPrice() {
        return propertyUnitPrice;
    }

    public void setPropertyUnitPrice(Double propertyUnitPrice) {
        this.propertyUnitPrice = propertyUnitPrice;
    }

    public Long getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Long propertyType) {
        this.propertyType = propertyType;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getContactDuty() {
        return contactDuty;
    }

    public void setContactDuty(String contactDuty) {
        this.contactDuty = contactDuty;
    }

    public String getFounderIntroduce() {
        return founderIntroduce;
    }

    public void setFounderIntroduce(String founderIntroduce) {
        this.founderIntroduce = founderIntroduce;
    }

    public Long getFoundingTime() {
        return foundingTime;
    }

    public void setFoundingTime(Long foundingTime) {
        this.foundingTime = foundingTime;
    }

    public Long getRegistrationTypeId() {
        return registrationTypeId;
    }

    public void setRegistrationTypeId(Long registrationTypeId) {
        this.registrationTypeId = registrationTypeId;
    }

    public Long getTechnicalFieldId() {
        return technicalFieldId;
    }

    public void setTechnicalFieldId(Long technicalFieldId) {
        this.technicalFieldId = technicalFieldId;
    }

    public Long getTaxpayerTypeId() {
        return taxpayerTypeId;
    }

    public void setTaxpayerTypeId(Long taxpayerTypeId) {
        this.taxpayerTypeId = taxpayerTypeId;
    }

    public Long getRelationWillingId() {
        return relationWillingId;
    }

    public void setRelationWillingId(Long relationWillingId) {
        this.relationWillingId = relationWillingId;
    }

    public Long getHighAndNewTechId() {
        return highAndNewTechId;
    }

    public void setHighAndNewTechId(Long highAndNewTechId) {
        this.highAndNewTechId = highAndNewTechId;
    }

    public Long getEntrepreneurialCharacteristicsId() {
        return entrepreneurialCharacteristicsId;
    }

    public void setEntrepreneurialCharacteristicsId(Long entrepreneurialCharacteristicsId) {
        this.entrepreneurialCharacteristicsId = entrepreneurialCharacteristicsId;
    }

    public Long getSerialEntrepreneurId() {
        return serialEntrepreneurId;
    }

    public void setSerialEntrepreneurId(Long serialEntrepreneurId) {
        this.serialEntrepreneurId = serialEntrepreneurId;
    }

    public BigDecimal getRiskInvestmentAmount() {
        return riskInvestmentAmount;
    }

    public void setRiskInvestmentAmount(BigDecimal riskInvestmentAmount) {
        this.riskInvestmentAmount = riskInvestmentAmount;
    }

    public Byte getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Byte deviceType) {
        this.deviceType = deviceType;
    }

    public String getUnifiedSocialCreditCode() {
        return unifiedSocialCreditCode;
    }

    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
    }

    public String getPostUri() {
        return postUri;
    }

    public void setPostUri(String postUri) {
        this.postUri = postUri;
    }

    public List<AttachmentDescriptor> getBanner() {
        return banner;
    }

    public void setBanner(List<AttachmentDescriptor> banner) {
        this.banner = banner;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getOriginPotentialSourceId() {
        return originPotentialSourceId;
    }

    public void setOriginPotentialSourceId(Long originPotentialSourceId) {
        this.originPotentialSourceId = originPotentialSourceId;
    }

    public Long getBuyOrLeaseItemId() {
        return buyOrLeaseItemId;
    }

    public void setBuyOrLeaseItemId(Long buyOrLeaseItemId) {
        this.buyOrLeaseItemId = buyOrLeaseItemId;
    }

    public String getBuyOrLeaseItemName() {
        return buyOrLeaseItemName;
    }

    public void setBuyOrLeaseItemName(String buyOrLeaseItemName) {
        this.buyOrLeaseItemName = buyOrLeaseItemName;
    }

    public String getBizAddress() {
        return bizAddress;
    }

    public void setBizAddress(String bizAddress) {
        this.bizAddress = bizAddress;
    }

    public String getBizLife() {
        return bizLife;
    }

    public void setBizLife(String bizLife) {
        this.bizLife = bizLife;
    }

    public String getCustomerIntentionLevel() {
        return customerIntentionLevel;
    }

    public void setCustomerIntentionLevel(String customerIntentionLevel) {
        this.customerIntentionLevel = customerIntentionLevel;
    }

    public String getEnterDevGoal() {
        return enterDevGoal;
    }

    public void setEnterDevGoal(String enterDevGoal) {
        this.enterDevGoal = enterDevGoal;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getControllerSunBirth() {
        return controllerSunBirth;
    }

    public void setControllerSunBirth(String controllerSunBirth) {
        this.controllerSunBirth = controllerSunBirth;
    }

    public String getControllerLunarBirth() {
        return controllerLunarBirth;
    }

    public void setControllerLunarBirth(String controllerLunarBirth) {
        this.controllerLunarBirth = controllerLunarBirth;
    }

    public Long getFinancingDemandItemId() {
        return financingDemandItemId;
    }

    public void setFinancingDemandItemId(Long financingDemandItemId) {
        this.financingDemandItemId = financingDemandItemId;
    }

    public String getFinancingDemandItemName() {
        return financingDemandItemName;
    }

    public void setFinancingDemandItemName(String financingDemandItemName) {
        this.financingDemandItemName = financingDemandItemName;
    }

    public String getStringTag1() {
        return stringTag1;
    }

    public void setStringTag1(String stringTag1) {
        this.stringTag1 = stringTag1;
    }

    public String getStringTag2() {
        return stringTag2;
    }

    public void setStringTag2(String stringTag2) {
        this.stringTag2 = stringTag2;
    }

    public String getStringTag3() {
        return stringTag3;
    }

    public void setStringTag3(String stringTag3) {
        this.stringTag3 = stringTag3;
    }

    public String getStringTag4() {
        return stringTag4;
    }

    public void setStringTag4(String stringTag4) {
        this.stringTag4 = stringTag4;
    }

    public String getStringTag5() {
        return stringTag5;
    }

    public void setStringTag5(String stringTag5) {
        this.stringTag5 = stringTag5;
    }

    public String getStringTag6() {
        return stringTag6;
    }

    public void setStringTag6(String stringTag6) {
        this.stringTag6 = stringTag6;
    }

    public String getStringTag7() {
        return stringTag7;
    }

    public void setStringTag7(String stringTag7) {
        this.stringTag7 = stringTag7;
    }

    public String getStringTag8() {
        return stringTag8;
    }

    public void setStringTag8(String stringTag8) {
        this.stringTag8 = stringTag8;
    }

    public String getStringTag9() {
        return stringTag9;
    }

    public void setStringTag9(String stringTag9) {
        this.stringTag9 = stringTag9;
    }

    public String getStringTag10() {
        return stringTag10;
    }

    public void setStringTag10(String stringTag10) {
        this.stringTag10 = stringTag10;
    }

    public String getStringTag11() {
        return stringTag11;
    }

    public void setStringTag11(String stringTag11) {
        this.stringTag11 = stringTag11;
    }

    public String getStringTag12() {
        return stringTag12;
    }

    public void setStringTag12(String stringTag12) {
        this.stringTag12 = stringTag12;
    }

    public String getStringTag13() {
        return stringTag13;
    }

    public void setStringTag13(String stringTag13) {
        this.stringTag13 = stringTag13;
    }

    public String getStringTag14() {
        return stringTag14;
    }

    public void setStringTag14(String stringTag14) {
        this.stringTag14 = stringTag14;
    }

    public String getStringTag15() {
        return stringTag15;
    }

    public void setStringTag15(String stringTag15) {
        this.stringTag15 = stringTag15;
    }

    public String getStringTag16() {
        return stringTag16;
    }

    public void setStringTag16(String stringTag16) {
        this.stringTag16 = stringTag16;
    }

    public Long getDropBox1ItemId() {
        return dropBox1ItemId;
    }

    public void setDropBox1ItemId(Long dropBox1ItemId) {
        this.dropBox1ItemId = dropBox1ItemId;
    }

    public Long getDropBox2ItemId() {
        return dropBox2ItemId;
    }

    public void setDropBox2ItemId(Long dropBox2ItemId) {
        this.dropBox2ItemId = dropBox2ItemId;
    }

    public Long getDropBox3ItemId() {
        return dropBox3ItemId;
    }

    public void setDropBox3ItemId(Long dropBox3ItemId) {
        this.dropBox3ItemId = dropBox3ItemId;
    }

    public Long getDropBox4ItemId() {
        return dropBox4ItemId;
    }

    public void setDropBox4ItemId(Long dropBox4ItemId) {
        this.dropBox4ItemId = dropBox4ItemId;
    }

    public Long getDropBox5ItemId() {
        return dropBox5ItemId;
    }

    public void setDropBox5ItemId(Long dropBox5ItemId) {
        this.dropBox5ItemId = dropBox5ItemId;
    }

    public Long getDropBox6ItemId() {
        return dropBox6ItemId;
    }

    public void setDropBox6ItemId(Long dropBox6ItemId) {
        this.dropBox6ItemId = dropBox6ItemId;
    }

    public Long getDropBox7ItemId() {
        return dropBox7ItemId;
    }

    public void setDropBox7ItemId(Long dropBox7ItemId) {
        this.dropBox7ItemId = dropBox7ItemId;
    }

    public Long getDropBox8ItemId() {
        return dropBox8ItemId;
    }

    public void setDropBox8ItemId(Long dropBox8ItemId) {
        this.dropBox8ItemId = dropBox8ItemId;
    }

    public Long getDropBox9ItemId() {
        return dropBox9ItemId;
    }

    public void setDropBox9ItemId(Long dropBox9ItemId) {
        this.dropBox9ItemId = dropBox9ItemId;
    }

    public String getDropBox1ItemName() {
        return dropBox1ItemName;
    }

    public void setDropBox1ItemName(String dropBox1ItemName) {
        this.dropBox1ItemName = dropBox1ItemName;
    }

    public String getDropBox2ItemName() {
        return dropBox2ItemName;
    }

    public void setDropBox2ItemName(String dropBox2ItemName) {
        this.dropBox2ItemName = dropBox2ItemName;
    }

    public String getDropBox3ItemName() {
        return dropBox3ItemName;
    }

    public void setDropBox3ItemName(String dropBox3ItemName) {
        this.dropBox3ItemName = dropBox3ItemName;
    }

    public String getDropBox4ItemName() {
        return dropBox4ItemName;
    }

    public void setDropBox4ItemName(String dropBox4ItemName) {
        this.dropBox4ItemName = dropBox4ItemName;
    }

    public String getDropBox5ItemName() {
        return dropBox5ItemName;
    }

    public void setDropBox5ItemName(String dropBox5ItemName) {
        this.dropBox5ItemName = dropBox5ItemName;
    }

    public String getDropBox6ItemName() {
        return dropBox6ItemName;
    }

    public void setDropBox6ItemName(String dropBox6ItemName) {
        this.dropBox6ItemName = dropBox6ItemName;
    }

    public String getDropBox7ItemName() {
        return dropBox7ItemName;
    }

    public void setDropBox7ItemName(String dropBox7ItemName) {
        this.dropBox7ItemName = dropBox7ItemName;
    }

    public String getDropBox8ItemName() {
        return dropBox8ItemName;
    }

    public void setDropBox8ItemName(String dropBox8ItemName) {
        this.dropBox8ItemName = dropBox8ItemName;
    }

    public String getDropBox9ItemName() {
        return dropBox9ItemName;
    }

    public void setDropBox9ItemName(String dropBox9ItemName) {
        this.dropBox9ItemName = dropBox9ItemName;
    }

    public Long getAptitudeFlagItemId() {
        return aptitudeFlagItemId;
    }

    public void setAptitudeFlagItemId(Long aptitudeFlagItemId) {
        this.aptitudeFlagItemId = aptitudeFlagItemId;
    }

    public String getAptitudeFlagItemName() {
        return aptitudeFlagItemName;
    }

    public void setAptitudeFlagItemName(String aptitudeFlagItemName) {
        this.aptitudeFlagItemName = aptitudeFlagItemName;
    }

    public List<CustomerAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<CustomerAttachmentDTO> attachments) {
        this.attachments = attachments;
    }


    public List<CustomerContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<CustomerContactDTO> contacts) {
        this.contacts = contacts;
    }

    public List<CustomerTrackerDTO> getTrackers() {
        return trackers;
    }

    public void setTrackers(List<CustomerTrackerDTO> trackers) {
        this.trackers = trackers;
    }



    public CustomerRequirementDTO getRequirement() {
        return requirement;
    }

    public void setRequirement(CustomerRequirementDTO requirement) {
        this.requirement = requirement;
    }

    public CustomerCurrentRentDTO getCurrentRent() {
        return currentRent;
    }

    public void setCurrentRent(CustomerCurrentRentDTO currentRent) {
        this.currentRent = currentRent;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
