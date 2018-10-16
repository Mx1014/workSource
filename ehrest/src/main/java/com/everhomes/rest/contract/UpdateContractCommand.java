package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>id: 合同id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>parentId: 原合同id</li>
 *     <li>rootParentId: 初始合同id</li>
 *     <li>customerType: 客户类型 0: organization; 1: individual</li>
 *     <li>customerId: 客户id</li>
 *     <li>customerName: 客户名称</li>
 *     <li>contractType: 合同类型 参考{@link com.everhomes.rest.contract.ContractType}</li>
 *     <li>contractNumber: 合同编码</li>
 *     <li>contractStartDate: 合同开始日期 时间戳</li>
 *     <li>contractEndDate: 合同结束日期 时间戳</li>
 *     <li>rentCycle: 租赁周期</li>
 *     <li>name: 合同名称</li>
 *     <li>partyAType: 甲方类型 0: organization; 1: individual</li>
 *     <li>partyAId: 甲方id</li>
 *     <li>contractSituation: 合同情况</li>
 *     <li>categoryItemId: 合同类型id </li>
 *     <li>filingPlace: 归档地</li>
 *     <li>recordNumber: 备案号</li>
 *     <li>signedTime: 签约日期 时间戳</li>
 *     <li>signedPurpose: 签约原因</li>
 *     <li>rentSize: 出租面积</li>
 *     <li>rent: 租金</li>
 *     <li>downpayment: 首付款</li>
 *     <li>downpaymentTime: 首付截止日期 时间戳</li>
 *     <li>deposit: 定金</li>
 *     <li>depositTime: 定金最迟收取日期 时间戳</li>
 *     <li>contractualPenalty: 违约金</li>
 *     <li>penaltyRemark: 违约说明</li>
 *     <li>commission: 佣金</li>
 *     <li>paidType: 付款方式</li>
 *     <li>freeDays: 免租期天数</li>
 *     <li>decorateBeginDate: 装修开始日期 时间戳</li>
 *     <li>decorateEndDate: 装修结束日期 时间戳</li>
 *     <li>freeParkingSpace: 赠送车位数量</li>
 *     <li>invalidReason: 作废原因</li>
 *     <li>denunciationReason: 退约原因</li>
 *     <li>apartments: 合同资产列表 参考{@link com.everhomes.rest.contract.BuildingApartmentDTO}</li>
 *     <li>chargingItems: 合同计价条款 参考{@link com.everhomes.rest.contract.ContractChargingItemDTO}</li>
 *     <li>attachments: 合同附件 参考{@link com.everhomes.rest.contract.ContractAttachmentDTO}</li>
 *     <li>status: 合同状态 1 待发起；3 审批中 参考{@link com.everhomes.rest.contract.ContractStatus}</li>
 *     <li>remark: 备注</li>
 *     <li>settled: 入住方</li>
 *     <li>layout: 房型</li>
 *     <li>adjusts: 调租计划列表 参考{@link com.everhomes.rest.contract.ContractChargingChangeDTO}</li>
 *     <li>frees: 免租计划列表 参考{@link com.everhomes.rest.contract.ContractChargingChangeDTO}</li>
 *     <li>denunciationUid: 退约人id</li>
 *     <li>denunciationName: 退约人姓名</li>
 *     <li>denunciationTime: 退约时间</li>
 *     <li>buildingRename: 房间别名</li>
 *     <li>categoryId: 合同类型多入口</li>
 *     <li>costGenerationMethod: 费用收取方式，0：按计费周期，1：按实际天数</li>
 *     <li>templateId: 合同模板id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/5.
 */
public class UpdateContractCommand {
    private Long id;
    private Integer namespaceId;
    private Long communityId;
    private Long orgId;
    private Long parentId;
    private Long rootParentId;
    private Long customerId;
    private String customerName;
    private String contractNumber;
    private Long contractEndDate;
    private Long contractStartDate;
    private Integer rentCycle;
    private String name;
    private Byte contractType;
    private Byte partyAType;
    private Long partyAId;
    private Byte customerType;
    private String contractSituation;
    private Long categoryItemId;
    private String filingPlace;
    private String recordNumber;
    private String invalidReason;
    private Long signedTime;
    private Double rentSize;
    private BigDecimal rent;
    private BigDecimal downpayment;
    private Long downpaymentTime;
    private BigDecimal deposit;
    private Long depositTime;
    private BigDecimal contractualPenalty;
    private String penaltyRemark;
    private BigDecimal commission;
    private String paidType;
    private Integer freeDays;
    private Integer freeParkingSpace;
    private Long decorateBeginDate;
    private Long decorateEndDate;
    private String signedPurpose;
    private String denunciationReason;
    private Long denunciationUid;
    private String denunciationName;
    private Long denunciationTime;
    private String buildingRename;
    private String remark;
    private Long createUid;
    private String layout;
    private String settled;
    private Long categoryId;
    private Byte costGenerationMethod;
    private Long templateId;

	@ItemType(BuildingApartmentDTO.class)
    private List<BuildingApartmentDTO> apartments;

    @ItemType(ContractChargingItemDTO.class)
    private List<ContractChargingItemDTO> chargingItems;

    @ItemType(ContractAttachmentDTO.class)
    private List<ContractAttachmentDTO> attachments;

    @ItemType(ContractChargingChangeDTO.class)
    private List<ContractChargingChangeDTO> adjusts;
    @ItemType(ContractChargingChangeDTO.class)
    private List<ContractChargingChangeDTO> frees;

    public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

    public Byte getCostGenerationMethod() {
		return costGenerationMethod;
	}

	public void setCostGenerationMethod(Byte costGenerationMethod) {
		this.costGenerationMethod = costGenerationMethod;
	}
	
    public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

    public Long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Long createUid) {
        this.createUid = createUid;
    }

    public Integer getRentCycle() {
        return rentCycle;
    }

    public void setRentCycle(Integer rentCycle) {
        this.rentCycle = rentCycle;
    }

    private Byte status;

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

    public Long getDenunciationTime() {
        return denunciationTime;
    }

    public void setDenunciationTime(Long denunciationTime) {
        this.denunciationTime = denunciationTime;
    }

    public Long getDenunciationUid() {
        return denunciationUid;
    }

    public void setDenunciationUid(Long denunciationUid) {
        this.denunciationUid = denunciationUid;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getSettled() {
        return settled;
    }

    public void setSettled(String settled) {
        this.settled = settled;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<BuildingApartmentDTO> getApartments() {
        return apartments;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setApartments(List<BuildingApartmentDTO> apartments) {
        this.apartments = apartments;
    }

    public List<ContractAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ContractAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public List<ContractChargingItemDTO> getChargingItems() {
        return chargingItems;
    }

    public void setChargingItems(List<ContractChargingItemDTO> chargingItems) {
        this.chargingItems = chargingItems;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCategoryItemId() {
        return categoryItemId;
    }

    public void setCategoryItemId(Long categoryItemId) {
        this.categoryItemId = categoryItemId;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Long contractEndDate) {
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

    public Long getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Long contractStartDate) {
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

    public Long getDecorateBeginDate() {
        return decorateBeginDate;
    }

    public void setDecorateBeginDate(Long decorateBeginDate) {
        this.decorateBeginDate = decorateBeginDate;
    }

    public Long getDecorateEndDate() {
        return decorateEndDate;
    }

    public void setDecorateEndDate(Long decorateEndDate) {
        this.decorateEndDate = decorateEndDate;
    }

    public String getDenunciationReason() {
        return denunciationReason;
    }

    public void setDenunciationReason(String denunciationReason) {
        this.denunciationReason = denunciationReason;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public Long getDepositTime() {
        return depositTime;
    }

    public void setDepositTime(Long depositTime) {
        this.depositTime = depositTime;
    }

    public BigDecimal getDownpayment() {
        return downpayment;
    }

    public void setDownpayment(BigDecimal downpayment) {
        this.downpayment = downpayment;
    }

    public Long getDownpaymentTime() {
        return downpaymentTime;
    }

    public void setDownpaymentTime(Long downpaymentTime) {
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

    public Long getSignedTime() {
        return signedTime;
    }

    public void setSignedTime(Long signedTime) {
        this.signedTime = signedTime;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
