package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.investment.CustomerContactDTO;
import com.everhomes.rest.investment.CustomerCurrentRentDTO;
import com.everhomes.rest.investment.CustomerRequirementDTO;
import com.everhomes.rest.investment.CustomerTrackerDTO;
import com.everhomes.rest.organization.OrganizationContactDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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
 *     <li>trackingUid: 跟进人UID</li>
 *     <li>trackingName: 跟进人姓名</li>
 *     <li>trackingPhone: 跟进人电话</li>
 *     <li>propertyArea: 资产面积</li>
 *     <li>propertyUnitPrice: 资产单价</li>
 *     <li>propertyType: 资产类型</li>
 *     <li>propertyTypeName: 资产类型名称</li>
 *     <li>longitude: 经度</li>
 *     <li>latitude: 纬度</li>
 *     <li>contactDuty: 联系人职务</li>
 *     <li>signedUpCount: 注册人数</li>
 *     <li>syncErrorMsg: 获取错误日志结果</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class EnterpriseCustomerDTO  implements Comparable<EnterpriseCustomerDTO>{
    private Long id;
    private Long ownerId;
    private String ownerType;
    private Long enterpriseId;
    private Long organizationId;
    private Long communityId;
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
    private Timestamp corpEntryDate;
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
    
    private Long trackingUid;
    private String trackingName;
    private Double propertyArea;
    private Double propertyUnitPrice;
    private Long propertyType;
    private String  propertyTypeName;
    private Double longitude;
    private Double latitude;
    private String contactDuty;

    private String founderIntroduce;
    private Timestamp foundingTime;
    private Long registrationTypeId;
    private String registrationTypeName;
    private Long technicalFieldId;
    private String technicalFieldName;
    private Long taxpayerTypeId;
    private String taxpayerTypeName;
    private Long relationWillingId;
    private String relationWillingName;
    private Long highAndNewTechId;
    private String highAndNewTechName;
    private Long entrepreneurialCharacteristicsId;
    private String entrepreneurialCharacteristicsName;
    private Long serialEntrepreneurId;
    private String serialEntrepreneurName;
    private BigDecimal riskInvestmentAmount;
    private Boolean thirdPartFlag = false;

    private Integer trackingPeriod;
    private String deviceType;
    private String trackingPhone;
    private String hotline;
    private String unifiedSocialCreditCode;
    private Integer signedUpCount;
    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> banner;
    private String postUri;
    private String postUrl;
    private Long sourceId;
    private String sourceType;

    private Long aptitudeFlagItemId;
    private String aptitudeFlagItemName;

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


    private String syncErrorMsg;

    private Timestamp createTime;

    private Timestamp updateTime;

    @ItemType(OrganizationContactDTO.class)
    private List<OrganizationContactDTO> enterpriseAdmins;
    @ItemType(CustomerEntryInfoDTO.class)
    private  List<CustomerEntryInfoDTO> entryInfos;

    @ItemType(CustomerAttachmentDTO.class)
    private List<CustomerAttachmentDTO> attachments;

    private String transactionRatio;
    private Long expectedSignDate;
    private Byte customerSource;
    private Long entryStatusItemId;
    private String entryStatusItemName;

    @ItemType(CreateCustomerTrackingCommand.class)
    private List<CreateCustomerTrackingCommand> trackingInfos;
    @ItemType(CustomerContactDTO.class)
    private List<CustomerContactDTO>  contacts ;
    @ItemType(CustomerTrackerDTO.class)
    private List<CustomerTrackerDTO> trackers;
    private CustomerRequirementDTO requirement;
    private CustomerCurrentRentDTO currentRent;


    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public List<CreateCustomerTrackingCommand> getTrackingInfos() {
        return trackingInfos;
    }

    public void setTrackingInfos(List<CreateCustomerTrackingCommand> trackingInfos) {
        this.trackingInfos = trackingInfos;
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

    public String getTransactionRatio() {
        return transactionRatio;
    }

    public void setTransactionRatio(String transactionRatio) {
        this.transactionRatio = transactionRatio;
    }

    public Long getExpectedSignDate() {
        return expectedSignDate;
    }

    public void setExpectedSignDate(Long expectedSignDate) {
        this.expectedSignDate = expectedSignDate;
    }

    public Byte getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(Byte customerSource) {
        this.customerSource = customerSource;
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

    public String getSyncErrorMsg() {
        return syncErrorMsg;
    }

    public void setSyncErrorMsg(String syncErrorMsg) {
        this.syncErrorMsg = syncErrorMsg;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Boolean getThirdPartFlag() {
        return thirdPartFlag;
    }

    public void setThirdPartFlag(Boolean thirdPartFlag) {
        this.thirdPartFlag = thirdPartFlag;
    }

    public Long getEntrepreneurialCharacteristicsId() {
        return entrepreneurialCharacteristicsId;
    }

    public void setEntrepreneurialCharacteristicsId(Long entrepreneurialCharacteristicsId) {
        this.entrepreneurialCharacteristicsId = entrepreneurialCharacteristicsId;
    }

    public String getEntrepreneurialCharacteristicsName() {
        return entrepreneurialCharacteristicsName;
    }

    public void setEntrepreneurialCharacteristicsName(String entrepreneurialCharacteristicsName) {
        this.entrepreneurialCharacteristicsName = entrepreneurialCharacteristicsName;
    }

    public String getFounderIntroduce() {
        return founderIntroduce;
    }

    public void setFounderIntroduce(String founderIntroduce) {
        this.founderIntroduce = founderIntroduce;
    }

    public Timestamp getFoundingTime() {
        return foundingTime;
    }

    public void setFoundingTime(Timestamp foundingTime) {
        this.foundingTime = foundingTime;
    }

    public Long getHighAndNewTechId() {
        return highAndNewTechId;
    }

    public void setHighAndNewTechId(Long highAndNewTechId) {
        this.highAndNewTechId = highAndNewTechId;
    }

    public String getHighAndNewTechName() {
        return highAndNewTechName;
    }

    public void setHighAndNewTechName(String highAndNewTechName) {
        this.highAndNewTechName = highAndNewTechName;
    }

    public Long getRegistrationTypeId() {
        return registrationTypeId;
    }

    public void setRegistrationTypeId(Long registrationTypeId) {
        this.registrationTypeId = registrationTypeId;
    }

    public String getRegistrationTypeName() {
        return registrationTypeName;
    }

    public void setRegistrationTypeName(String registrationTypeName) {
        this.registrationTypeName = registrationTypeName;
    }

    public Long getRelationWillingId() {
        return relationWillingId;
    }

    public void setRelationWillingId(Long relationWillingId) {
        this.relationWillingId = relationWillingId;
    }

    public String getRelationWillingName() {
        return relationWillingName;
    }

    public void setRelationWillingName(String relationWillingName) {
        this.relationWillingName = relationWillingName;
    }

    public BigDecimal getRiskInvestmentAmount() {
        return riskInvestmentAmount;
    }

    public void setRiskInvestmentAmount(BigDecimal riskInvestmentAmount) {
        this.riskInvestmentAmount = riskInvestmentAmount;
    }

    public Long getSerialEntrepreneurId() {
        return serialEntrepreneurId;
    }

    public void setSerialEntrepreneurId(Long serialEntrepreneurId) {
        this.serialEntrepreneurId = serialEntrepreneurId;
    }

    public String getSerialEntrepreneurName() {
        return serialEntrepreneurName;
    }

    public void setSerialEntrepreneurName(String serialEntrepreneurName) {
        this.serialEntrepreneurName = serialEntrepreneurName;
    }

    public Long getTaxpayerTypeId() {
        return taxpayerTypeId;
    }

    public void setTaxpayerTypeId(Long taxpayerTypeId) {
        this.taxpayerTypeId = taxpayerTypeId;
    }

    public String getTaxpayerTypeName() {
        return taxpayerTypeName;
    }

    public void setTaxpayerTypeName(String taxpayerTypeName) {
        this.taxpayerTypeName = taxpayerTypeName;
    }

    public Long getTechnicalFieldId() {
        return technicalFieldId;
    }

    public void setTechnicalFieldId(Long technicalFieldId) {
        this.technicalFieldId = technicalFieldId;
    }

    public String getTechnicalFieldName() {
        return technicalFieldName;
    }

    public void setTechnicalFieldName(String technicalFieldName) {
        this.technicalFieldName = technicalFieldName;
    }

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

    public Timestamp getCorpEntryDate() {
        return corpEntryDate;
    }

    public void setCorpEntryDate(Timestamp corpEntryDate) {
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

	public String getTrackingName() {
		return trackingName;
	}

	public void setTrackingName(String trackingName) {
		this.trackingName = trackingName;
	}

	public String getPropertyTypeName() {
		return propertyTypeName;
	}

	public void setPropertyTypeName(String propertyTypeName) {
		this.propertyTypeName = propertyTypeName;
	}

	public String getContactDuty() {
		return contactDuty;
	}

	public void setContactDuty(String contactDuty) {
		this.contactDuty = contactDuty;
	}

    public String getTrackingPhone() {
        return trackingPhone;
    }

    public void setTrackingPhone(String trackingPhone) {
        this.trackingPhone = trackingPhone;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getTrackingPeriod() {
        return trackingPeriod;
    }

    public void setTrackingPeriod(Integer trackingPeriod) {
        this.trackingPeriod = trackingPeriod;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getUnifiedSocialCreditCode() {
        return unifiedSocialCreditCode;
    }

    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
    }

    public List<AttachmentDescriptor> getBanner() {
        return banner;
    }

    public void setBanner(List<AttachmentDescriptor> banner) {
        this.banner = banner;
    }

    public String getPostUri() {
        return postUri;
    }

    public void setPostUri(String postUri) {
        this.postUri = postUri;
    }

    public List<OrganizationContactDTO> getEnterpriseAdmins() {
        return enterpriseAdmins;
    }

    public void setEnterpriseAdmins(List<OrganizationContactDTO> enterpriseAdmins) {
        this.enterpriseAdmins = enterpriseAdmins;
    }

    public List<CustomerEntryInfoDTO> getEntryInfos() {
        return entryInfos;
    }

    public void setEntryInfos(List<CustomerEntryInfoDTO> entryInfos) {
        this.entryInfos = entryInfos;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public Integer getSignedUpCount() {
        return signedUpCount;
    }

    public void setSignedUpCount(Integer signedUpCount) {
        this.signedUpCount = signedUpCount;
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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<CustomerAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<CustomerAttachmentDTO> attachments) {
        this.attachments = attachments;
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

    public Long getBuyOrLeaseItemId() {
        return buyOrLeaseItemId;
    }

    public void setBuyOrLeaseItemId(Long buyOrLeaseItemId) {
        this.buyOrLeaseItemId = buyOrLeaseItemId;
    }

    public Long getFinancingDemandItemId() {
        return financingDemandItemId;
    }

    public void setFinancingDemandItemId(Long financingDemandItemId) {
        this.financingDemandItemId = financingDemandItemId;
    }

    public String getBuyOrLeaseItemName() {
        return buyOrLeaseItemName;
    }

    public void setBuyOrLeaseItemName(String buyOrLeaseItemName) {
        this.buyOrLeaseItemName = buyOrLeaseItemName;
    }

    public String getFinancingDemandItemName() {
        return financingDemandItemName;
    }

    public void setFinancingDemandItemName(String financingDemandItemName) {
        this.financingDemandItemName = financingDemandItemName;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    @Override
	public int compareTo(EnterpriseCustomerDTO o) {
		return o.getId() - this.getId() >= 0 ? 1 : -1;
	}

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
