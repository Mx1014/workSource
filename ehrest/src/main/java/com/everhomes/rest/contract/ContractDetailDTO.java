package com.everhomes.rest.contract;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.flow.FlowCaseEntity;

/**
 * <ul>
 *     <li>id: 合同id</li>
 *     <li>parentId: 原合同id</li>
 *     <li>rootParentId: 初始合同id</li>
 *     <li>customerType: 客户类型 0: organization; 1: individual</li>
 *     <li>customerId: 客户id</li>
 *     <li>customerName: 客户名称</li>
 *     <li>contractType: 合同类型 参考{@link com.everhomes.rest.contract.ContractType}</li>
 *     <li>contractNumber: 合同编码</li>
 *     <li>contractStartDate: 合同开始日期</li>
 *     <li>contractEndDate: 合同结束日期</li>
 *     <li>rentCycle: 租赁周期</li>
 *     <li>name: 合同名称</li>
 *     <li>partyAType: 甲方类型 0: organization; 1: individual</li>
 *     <li>partyAId: 甲方id</li>
 *     <li>partyAName: 甲方名称</li>
 *     <li>contractSituation: 合同情况</li>
 *     <li>categoryItemId: 合同类型id </li>
 *     <li>categoryItemName: 合同类型 </li>
 *     <li>filingPlace: 归档地</li>
 *     <li>recordNumber: 备案号</li>
 *     <li>signedTime: 签约日期</li>
 *     <li>signedPurpose: 签约原因</li>
 *     <li>rentSize: 出租面积</li>
 *     <li>rent: 租金</li>
 *     <li>downpayment: 首付款</li>
 *     <li>downpaymentTime: 首付截止日期</li>
 *     <li>deposit: 定金</li>
 *     <li>depositTime: 定金最迟收取日期</li>
 *     <li>contractualPenalty: 违约金</li>
 *     <li>penaltyRemark: 违约说明</li>
 *     <li>commission: 佣金</li>
 *     <li>paidType: 付款方式</li>
 *     <li>freeDays: 免租期天数</li>
 *     <li>decorateBeginDate: 装修开始日期</li>
 *     <li>decorateEndDate: 装修结束日期</li>
 *     <li>freeParkingSpace: 赠送车位数量</li>
 *     <li>invalidUid: 作废人id</li>
 *     <li>invalidUserName: 作废人</li>
 *     <li>invalidTime: 作废时间</li>
 *     <li>invalidReason: 作废原因</li>
 *     <li>denunciationReason: 退约原因</li>
 *     <li>apartments: 合同资产列表 参考{@link com.everhomes.rest.contract.BuildingApartmentDTO}</li>
 *     <li>chargingItems: 合同计价条款 参考{@link com.everhomes.rest.contract.ContractChargingItemDTO}</li>
 *     <li>attachments: 合同附件 参考{@link com.everhomes.rest.contract.ContractAttachmentDTO}</li>
 *     <li>status: 合同状态 参考{@link com.everhomes.rest.contract.ContractStatus}</li>
 *     <li>remark: 备注</li>
 *     <li>layout: 房型（房型改为了选项类型）</li>
 *     <li>layoutName: 房型名</li>
 *     <li>adjusts: 调租计划列表 参考{@link com.everhomes.rest.contract.ContractChargingChangeDTO}</li>
 *     <li>frees: 免租计划列表 参考{@link com.everhomes.rest.contract.ContractChargingChangeDTO}</li>
 *     <li>denunciationUid: 退约人id</li>
 *     <li>denunciationName: 退约人姓名</li>
 *     <li>denunciationTime: 退约时间</li>
 *     <li>buildingRename: 房间别名</li>
 *     <li>contractTemplate: 合同模板信息{@link com.everhomes.rest.contract.ContractTemplateDTO}</li>
 *     <li>templateId: 合同模板id</li>
 *     <li>templateName: 合同模板名称</li>
 *     <li>costGenerationMethod: 费用截断方式，0：按计费周期，1：按实际天数</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class ContractDetailDTO {
    private Long id;
    private Long parentId;
    private String parentContractNumber;
    private Long rootParentId;
    private String rootContractNumber;
    private Long customerId;
    private String customerName;
    private String contractNumber;
    private Timestamp contractEndDate;
    private Timestamp contractStartDate;
    private Integer rentCycle;
    private String name;
    private Byte contractType;
    private Byte partyAType;
    private Long partyAId;
    private String partyAName;
    private Byte customerType;
    private String contractSituation;
    private Long categoryItemId;
    private String categoryItemName;
    private String filingPlace;
    private String recordNumber;
    private Long invalidUid;
    private String invalidUserName;
    private Timestamp invalidTime;
    private String invalidReason;
    private Timestamp signedTime;
    private Double rentSize;
    private BigDecimal rent;
    private BigDecimal downpayment;
    private Timestamp downpaymentTime;
    private BigDecimal deposit;
    private Timestamp depositTime;
    private BigDecimal contractualPenalty;
    private String penaltyRemark;
    private BigDecimal commission;
    private String paidType;
    private Integer freeDays;
    private Integer freeParkingSpace;
    private Timestamp decorateBeginDate;
    private Timestamp decorateEndDate;
    private String signedPurpose;
    private String denunciationReason;
    private Long denunciationUid;
    private String denunciationName;
    private Timestamp denunciationTime;
    private String buildingRename;
    private String remark;
    private Long createUid;
    private String creatorName;
    private String layout;
    private String layoutName;
    private String settled;
    private Byte status;
    private BigDecimal remainingAmount;
    private Long bidItemId;
    private Long createOrgId;
    private String createOrgName;
    private Long createPositionId;
    private String createPositionName;
    private String ourLegalRepresentative;
    private String taxpayerIdentificationCode;
    private String registeredAddress;
    private String registeredPhone;
    private String payee;
    private String payer;
    private String dueBank;
    private String bankAccount;
    private BigDecimal exchangeRate;
    private Integer ageLimit;
    private Long applicationId;
    private String applicationTheme;
    private Long paymentModeItemId;
    private Timestamp paidTime;
    private BigDecimal lumpSumPayment;
    private String treatyParticulars;
    private Byte paymentFlag;
    private Long templateId;
    private String templateName;
    //add by tangcen 2018年6月13日21:16:10
    private Byte costGenerationMethod;
    private String startTime;
	private String endTimeByPeriod;
	private String endTimeByDay;
	
	@ItemType(ChargingItemVariables.class)
    private List<ChargingItemVariables> chargingPaymentTypeVariables;
	
    public List<ChargingItemVariables> getChargingPaymentTypeVariables() {
		return chargingPaymentTypeVariables;
	}

    @ItemType(FlowCaseEntity.class)
    private List<FlowCaseEntity> price;
    
	public void setChargingPaymentTypeVariables(List<ChargingItemVariables> chargingPaymentTypeVariables) {
		this.chargingPaymentTypeVariables = chargingPaymentTypeVariables;
	}

	public String getEndTimeByPeriod() {
		return endTimeByPeriod;
	}

	public void setEndTimeByPeriod(String endTimeByPeriod) {
		this.endTimeByPeriod = endTimeByPeriod;
	}

	public String getEndTimeByDay() {
		return endTimeByDay;
	}

	public void setEndTimeByDay(String endTimeByDay) {
		this.endTimeByDay = endTimeByDay;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Byte getCostGenerationMethod() {
		return costGenerationMethod;
	}

	public void setCostGenerationMethod(Byte costGenerationMethod) {
		this.costGenerationMethod = costGenerationMethod;
	}

	@ItemType(ContractChargingChangeDTO.class)
    private List<ContractChargingChangeDTO> adjusts;
    @ItemType(ContractChargingChangeDTO.class)
    private List<ContractChargingChangeDTO> frees;
    
    List<FlowCaseEntity> entities = new ArrayList<>();

	public List<FlowCaseEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<FlowCaseEntity> entities) {
		this.entities = entities;
	}

	public List<FlowCaseEntity> getPrice() {
		return price;
	}

	public void setPrice(List<FlowCaseEntity> price) {
		this.price = price;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
		
    //add by tangcen
    private Long communityId;
    private Integer namespaceId;
    private Long categoryId;
    private Long documentId;
    private Timestamp apartmentDeliveryTime; 
    private Timestamp downPaymentRentTime; 
	private Integer monthlyMargin; 
	private BigDecimal marginAmount; 
	private BigDecimal preAmount; 
	private String contractingPlace; 
	
	
    public Timestamp getApartmentDeliveryTime() {
		return apartmentDeliveryTime;
	}

	public void setApartmentDeliveryTime(Timestamp apartmentDeliveryTime) {
		this.apartmentDeliveryTime = apartmentDeliveryTime;
	}

	public Timestamp getDownPaymentRentTime() {
		return downPaymentRentTime;
	}

	public void setDownPaymentRentTime(Timestamp downPaymentRentTime) {
		this.downPaymentRentTime = downPaymentRentTime;
	}

	public Integer getMonthlyMargin() {
		return monthlyMargin;
	}

	public void setMonthlyMargin(Integer monthlyMargin) {
		this.monthlyMargin = monthlyMargin;
	}

	public BigDecimal getMarginAmount() {
		return marginAmount;
	}

	public void setMarginAmount(BigDecimal marginAmount) {
		this.marginAmount = marginAmount;
	}

	public BigDecimal getPreAmount() {
		return preAmount;
	}

	public void setPreAmount(BigDecimal preAmount) {
		this.preAmount = preAmount;
	}

	public String getContractingPlace() {
		return contractingPlace;
	}

	public void setContractingPlace(String contractingPlace) {
		this.contractingPlace = contractingPlace;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    
    public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public List<ContractChargingChangeDTO> getAdjusts() {
        return adjusts;
    }

    public void setAdjusts(List<ContractChargingChangeDTO> adjusts) {
        this.adjusts = adjusts;
    }

    public List<ContractChargingChangeDTO> getFrees() {
        return frees;
    }

    public void setFrees(List<ContractChargingChangeDTO> frees) {
        this.frees = frees;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getSettled() {
        return settled;
    }

    public void setSettled(String settled) {
        this.settled = settled;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    @ItemType(BuildingApartmentDTO.class)
    private List<BuildingApartmentDTO> apartments;

    @ItemType(ContractChargingItemDTO.class)
    private List<ContractChargingItemDTO> chargingItems;

    @ItemType(ContractAttachmentDTO.class)
    private List<ContractAttachmentDTO> attachments;

    @ItemType(ContractPaymentPlanDTO.class)
    private List<ContractPaymentPlanDTO> plans;

    public List<ContractPaymentPlanDTO> getPlans() {
        return plans;
    }

    public void setPlans(List<ContractPaymentPlanDTO> plans) {
        this.plans = plans;
    }

    public Integer getRentCycle() {
        return rentCycle;
    }

    public void setRentCycle(Integer rentCycle) {
        this.rentCycle = rentCycle;
    }

    public Long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Long createUid) {
        this.createUid = createUid;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getParentContractNumber() {
        return parentContractNumber;
    }

    public void setParentContractNumber(String parentContractNumber) {
        this.parentContractNumber = parentContractNumber;
    }

    public String getRootContractNumber() {
        return rootContractNumber;
    }

    public void setRootContractNumber(String rootContractNumber) {
        this.rootContractNumber = rootContractNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ContractAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ContractAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public List<BuildingApartmentDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<BuildingApartmentDTO> apartments) {
        this.apartments = apartments;
    }

    public List<ContractChargingItemDTO> getChargingItems() {
        return chargingItems;
    }

    public void setChargingItems(List<ContractChargingItemDTO> chargingItems) {
        this.chargingItems = chargingItems;
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

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public Timestamp getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Timestamp contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContractSituation() {
        return contractSituation;
    }

    public void setContractSituation(String contractSituation) {
        this.contractSituation = contractSituation;
    }

    public Timestamp getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Timestamp contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Byte getContractType() {
        return contractType;
    }

    public void setContractType(Byte contractType) {
        this.contractType = contractType;
    }

    public BigDecimal getContractualPenalty() {
        return contractualPenalty;
    }

    public void setContractualPenalty(BigDecimal contractualPenalty) {
        this.contractualPenalty = contractualPenalty;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Timestamp getDecorateBeginDate() {
        return decorateBeginDate;
    }

    public void setDecorateBeginDate(Timestamp decorateBeginDate) {
        this.decorateBeginDate = decorateBeginDate;
    }

    public Timestamp getDecorateEndDate() {
        return decorateEndDate;
    }

    public void setDecorateEndDate(Timestamp decorateEndDate) {
        this.decorateEndDate = decorateEndDate;
    }

    public String getDenunciationReason() {
        return denunciationReason;
    }

    public void setDenunciationReason(String denunciationReason) {
        this.denunciationReason = denunciationReason;
    }

    public Timestamp getDenunciationTime() {
        return denunciationTime;
    }

    public void setDenunciationTime(Timestamp denunciationTime) {
        this.denunciationTime = denunciationTime;
    }

    public Long getDenunciationUid() {
        return denunciationUid;
    }

    public void setDenunciationUid(Long denunciationUid) {
        this.denunciationUid = denunciationUid;
    }

    public String getBuildingRename() {
        return buildingRename;
    }

    public void setBuildingRename(String buildingRename) {
        this.buildingRename = buildingRename;
    }

    public String getDenunciationName() {
        return denunciationName;
    }

    public void setDenunciationName(String denunciationName) {
        this.denunciationName = denunciationName;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public Timestamp getDepositTime() {
        return depositTime;
    }

    public void setDepositTime(Timestamp depositTime) {
        this.depositTime = depositTime;
    }

    public BigDecimal getDownpayment() {
        return downpayment;
    }

    public void setDownpayment(BigDecimal downpayment) {
        this.downpayment = downpayment;
    }

    public Timestamp getDownpaymentTime() {
        return downpaymentTime;
    }

    public void setDownpaymentTime(Timestamp downpaymentTime) {
        this.downpaymentTime = downpaymentTime;
    }

    public String getFilingPlace() {
        return filingPlace;
    }

    public void setFilingPlace(String filingPlace) {
        this.filingPlace = filingPlace;
    }

    public Integer getFreeDays() {
        return freeDays;
    }

    public void setFreeDays(Integer freeDays) {
        this.freeDays = freeDays;
    }

    public Integer getFreeParkingSpace() {
        return freeParkingSpace;
    }

    public void setFreeParkingSpace(Integer freeParkingSpace) {
        this.freeParkingSpace = freeParkingSpace;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    public Timestamp getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Timestamp invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Long getInvalidUid() {
        return invalidUid;
    }

    public void setInvalidUid(Long invalidUid) {
        this.invalidUid = invalidUid;
    }

    public String getInvalidUserName() {
        return invalidUserName;
    }

    public void setInvalidUserName(String invalidUserName) {
        this.invalidUserName = invalidUserName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaidType() {
        return paidType;
    }

    public void setPaidType(String paidType) {
        this.paidType = paidType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getPartyAId() {
        return partyAId;
    }

    public void setPartyAId(Long partyAId) {
        this.partyAId = partyAId;
    }

    public String getPartyAName() {
        return partyAName;
    }

    public void setPartyAName(String partyAName) {
        this.partyAName = partyAName;
    }

    public Byte getPartyAType() {
        return partyAType;
    }

    public void setPartyAType(Byte partyAType) {
        this.partyAType = partyAType;
    }

    public String getPenaltyRemark() {
        return penaltyRemark;
    }

    public void setPenaltyRemark(String penaltyRemark) {
        this.penaltyRemark = penaltyRemark;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public Double getRentSize() {
        return rentSize;
    }

    public void setRentSize(Double rentSize) {
        this.rentSize = rentSize;
    }

    public Long getRootParentId() {
        return rootParentId;
    }

    public void setRootParentId(Long rootParentId) {
        this.rootParentId = rootParentId;
    }

    public String getSignedPurpose() {
        return signedPurpose;
    }

    public void setSignedPurpose(String signedPurpose) {
        this.signedPurpose = signedPurpose;
    }

    public Timestamp getSignedTime() {
        return signedTime;
    }

    public void setSignedTime(Timestamp signedTime) {
        this.signedTime = signedTime;
    }

    public Integer getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(Integer ageLimit) {
        this.ageLimit = ageLimit;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Long getBidItemId() {
        return bidItemId;
    }

    public void setBidItemId(Long bidItemId) {
        this.bidItemId = bidItemId;
    }

    public Long getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(Long createOrgId) {
        this.createOrgId = createOrgId;
    }

    public Long getCreatePositionId() {
        return createPositionId;
    }

    public void setCreatePositionId(Long createPositionId) {
        this.createPositionId = createPositionId;
    }

    public String getDueBank() {
        return dueBank;
    }

    public void setDueBank(String dueBank) {
        this.dueBank = dueBank;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getLumpSumPayment() {
        return lumpSumPayment;
    }

    public void setLumpSumPayment(BigDecimal lumpSumPayment) {
        this.lumpSumPayment = lumpSumPayment;
    }

    public String getOurLegalRepresentative() {
        return ourLegalRepresentative;
    }

    public void setOurLegalRepresentative(String ourLegalRepresentative) {
        this.ourLegalRepresentative = ourLegalRepresentative;
    }

    public Timestamp getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Timestamp paidTime) {
        this.paidTime = paidTime;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public Byte getPaymentFlag() {
        return paymentFlag;
    }

    public void setPaymentFlag(Byte paymentFlag) {
        this.paymentFlag = paymentFlag;
    }

    public Long getPaymentModeItemId() {
        return paymentModeItemId;
    }

    public void setPaymentModeItemId(Long paymentModeItemId) {
        this.paymentModeItemId = paymentModeItemId;
    }

    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    public String getRegisteredPhone() {
        return registeredPhone;
    }

    public void setRegisteredPhone(String registeredPhone) {
        this.registeredPhone = registeredPhone;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getTaxpayerIdentificationCode() {
        return taxpayerIdentificationCode;
    }

    public void setTaxpayerIdentificationCode(String taxpayerIdentificationCode) {
        this.taxpayerIdentificationCode = taxpayerIdentificationCode;
    }

    public String getTreatyParticulars() {
        return treatyParticulars;
    }

    public void setTreatyParticulars(String treatyParticulars) {
        this.treatyParticulars = treatyParticulars;
    }

    public String getCreateOrgName() {
        return createOrgName;
    }

    public void setCreateOrgName(String createOrgName) {
        this.createOrgName = createOrgName;
    }

    public String getCreatePositionName() {
        return createPositionName;
    }

    public void setCreatePositionName(String createPositionName) {
        this.createPositionName = createPositionName;
    }

    public String getApplicationTheme() {
        return applicationTheme;
    }

    public void setApplicationTheme(String applicationTheme) {
        this.applicationTheme = applicationTheme;
    }

}
