package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>parentId: 原合同id</li>
 *     <li>rootParentId: 初始合同id</li>
 *     <li>customerType: 客户类型 0: organization; 1: individual; 3: supplier</li>
 *     <li>customerId: 客户id</li>
 *     <li>customerName: 客户名称</li>
 *     <li>contractType: 合同类型 参考{@link com.everhomes.rest.contract.ContractType}</li>
 *     <li>contractNumber: 合同编码</li>
 *     <li>contractStartDate: 合同开始日期</li>
 *     <li>contractEndDate: 合同结束日期</li>
 *     <li>name: 合同名称</li>
 *     <li>partyAType: 甲方类型 0: organization; 1: individual</li>
 *     <li>partyAId: 甲方id</li>
 *     <li>partyAName: 甲方名称</li>
 *     <li>categoryItemId: 合同类型id </li>
 *     <li>categoryItemName: 合同类型 </li>
 *     <li>signedTime: 签约日期</li>
 *     <li>signedPurpose: 签约原因</li>
 *     <li>rent: 合同总额</li>
 *     <li>remainingAmount: 剩余金额</li>
 *     <li>invalidUid: 作废人id</li>
 *     <li>invalidUserName: 作废人</li>
 *     <li>invalidTime: 作废时间</li>
 *     <li>invalidReason: 作废原因</li>
 *     <li>denunciationReason: 退约原因</li>
 *     <li>apartments: 合同资产列表 参考{@link com.everhomes.rest.contract.BuildingApartmentDTO}</li>
 *     <li>status: 合同状态 参考{@link com.everhomes.rest.contract.ContractStatus}</li>
 *     <li>remark: 备注</li>
 *     <li>denunciationUid: 退约人id</li>
 *     <li>denunciationName: 退约人姓名</li>
 *     <li>denunciationTime: 退约时间</li>
 *     <li>createUid: 创建人id</li>
 *     <li>creatorName: 创建人姓名</li>
 *     <li>createOrgId: 创建人部门id</li>
 *     <li>createOrgName: 创建人部门名</li>
 *     <li>createPositionId: 创建人岗位id</li>
 *     <li>createPositionName: 创建人岗位名</li>
 *     <li>ourLegalRepresentative: 我方法人代表</li>
 *     <li>taxpayerIdentificationCode: 纳税人识别码</li>
 *     <li>registeredAddress: 注册地址</li>
 *     <li>registeredPhone: 注册电话</li>
 *     <li>payee: 收款单位</li>
 *     <li>payer: 付款单位</li>
 *     <li>dueBank: 收款银行</li>
 *     <li>bankAccount: 银行账号</li>
 *     <li>exchangeRate: 兑换汇率</li>
 *     <li>ageLimit: 年限</li>
 *     <li>applicationId: 关联请示id</li>
 *     <li>applicationName: 关联请示名</li>
 *     <li>paymentModeItemId: 预计付款方式id</li>
 *     <li>paymentModeItemName: 预计付款方式</li>
 *     <li>lumpSumPayment: 一次性付款金额</li>
 *     <li>paidTime: 预计付款时间</li>
 *     <li>treatyParticulars: 合同摘要</li>
 *     <li>attachments: 附件</li>
 *     <li>plans: 付款计划</li>
 * </ul>
 * Created by ying.xiong on 2017/12/28.
 */
public class CreatePaymentContractCommand {
    private Long organizationId;
    private Long parentId;
    private Long rootParentId;
    private Byte contractType;
    private Integer namespaceId;
    private Long communityId;
    private Byte customerType;
    private Long customerId;
    private String customerName;
    private Byte partyAType;
    private Long partyAId;
    private String partyAName;
    private Long categoryItemId;
    private String categoryItemName;
    private String name;
    private String contractNumber;
    private Long contractStartDate;
    private Long contractEndDate;
    private BigDecimal rent;
    private String signedPurpose;
    private Long signedTime;
    private BigDecimal remainingAmount;
    private Long bidItemId;
    private Long createUid;
    private Long createOrgId;
    private Long createPositionId;
    private String creatorName;
    private String createOrgName;
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
    private String applicationName;
    private Long paymentModeItemId;
    private String paymentModeItemName;
    private BigDecimal lumpSumPayment;
    private Long paidTime;
    private String treatyParticulars;
    private Byte status;
    private String remark;
    private Long createTime;
    private Long invalidUid;
    private Long invalidTime;
    private String invalidReason;
    private Long reviewUid;
    private Long reviewTime;
    private String denunciationReason;
    private Long denunciationTime;
    private Long denunciationUid;
    
    private Long categoryId;
    public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@ItemType(ContractAttachmentDTO.class)
    private List<ContractAttachmentDTO> attachments;
    @ItemType(ContractPaymentPlanDTO.class)
    private List<ContractPaymentPlanDTO> plans;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public List<ContractAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ContractAttachmentDTO> attachments) {
        this.attachments = attachments;
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

    public Long getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(Long createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getCreateOrgName() {
        return createOrgName;
    }

    public void setCreateOrgName(String createOrgName) {
        this.createOrgName = createOrgName;
    }

    public Long getCreatePositionId() {
        return createPositionId;
    }

    public void setCreatePositionId(Long createPositionId) {
        this.createPositionId = createPositionId;
    }

    public String getCreatePositionName() {
        return createPositionName;
    }

    public void setCreatePositionName(String createPositionName) {
        this.createPositionName = createPositionName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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

    public String getDenunciationReason() {
        return denunciationReason;
    }

    public void setDenunciationReason(String denunciationReason) {
        this.denunciationReason = denunciationReason;
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

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    public Long getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Long invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Long getInvalidUid() {
        return invalidUid;
    }

    public void setInvalidUid(Long invalidUid) {
        this.invalidUid = invalidUid;
    }

    public BigDecimal getLumpSumPayment() {
        return lumpSumPayment;
    }

    public void setLumpSumPayment(BigDecimal lumpSumPayment) {
        this.lumpSumPayment = lumpSumPayment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOurLegalRepresentative() {
        return ourLegalRepresentative;
    }

    public void setOurLegalRepresentative(String ourLegalRepresentative) {
        this.ourLegalRepresentative = ourLegalRepresentative;
    }

    public Long getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Long paidTime) {
        this.paidTime = paidTime;
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

    public Long getPaymentModeItemId() {
        return paymentModeItemId;
    }

    public void setPaymentModeItemId(Long paymentModeItemId) {
        this.paymentModeItemId = paymentModeItemId;
    }

    public String getPaymentModeItemName() {
        return paymentModeItemName;
    }

    public void setPaymentModeItemName(String paymentModeItemName) {
        this.paymentModeItemName = paymentModeItemName;
    }

    public List<ContractPaymentPlanDTO> getPlans() {
        return plans;
    }

    public void setPlans(List<ContractPaymentPlanDTO> plans) {
        this.plans = plans;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public Long getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Long reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Long getReviewUid() {
        return reviewUid;
    }

    public void setReviewUid(Long reviewUid) {
        this.reviewUid = reviewUid;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
    
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
