package com.everhomes.rest.investment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.customer.CreateCustomerTrackingCommand;
import com.everhomes.rest.customer.CustomerAttachmentDTO;
import com.everhomes.rest.forum.AttachmentDescriptor;

import java.math.BigDecimal;
import java.util.List;

public class EnterpriseInvestmentDTO {

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


    @ItemType(CustomerAttachmentDTO.class)
    private List<CustomerAttachmentDTO> attachments;

    // we should add new module fields
    // investment enterprise tracking infos
    @ItemType(CreateCustomerTrackingCommand.class)
    private List<CreateCustomerTrackingCommand> trackingInfos;
    @ItemType(EnterpriseInvestmentContactDTO.class)
    private List<EnterpriseInvestmentContactDTO>  contacts ;
    @ItemType(EnterpriseInvestmentDemandDTO.class)
    private List<EnterpriseInvestmentDemandDTO> demands;
    @ItemType(EnterpriseInvestmentNowInfoDTO.class)
    private List<EnterpriseInvestmentNowInfoDTO> nowInfos;
}
