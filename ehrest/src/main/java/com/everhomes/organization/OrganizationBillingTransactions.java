package com.everhomes.organization;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *	<li>createTime : 缴费时间</li>
 *	<li>address : 楼栋门牌号</li>
 *	<li>ownerName : 业主姓名</li>
 *	<li>ownerTelephone : 业主电话</li>
 *	<li>chargeAmount : 实付金额</li>
 *	<li>description : 缴费说明</li>
 *	
 *</ul>
 *
 */
public class OrganizationBillingTransactions {
	
	private java.lang.Long       id;
	private java.lang.Long       txSequence;
	private java.lang.Byte       txType;
	private java.lang.Long       ownerId;
	private java.lang.Long       ownerAccountId;
	private java.lang.Byte       targetAccountType;
	private java.lang.Long       targetAccountId;
	private java.lang.Byte       billType;
	private java.lang.Long       billId;
	private java.math.BigDecimal chargeAmount;
	private java.lang.String     description;
	private java.lang.String     vendor;
	private java.lang.String     resultCodeScope;
	private java.lang.Integer    resultCodeId;
	private java.lang.String     resultDesc;
	private java.lang.Long       operatorUid;
	private java.lang.Byte       paidType;
	private java.sql.Timestamp   createTime;
	
	//expand
	private Long addressId;
	private String address;
	private String ownerTelephone;
	private String ownerName;
	
	public Long getAddressId() {
		return addressId;
	}
	public String getOwnerTelephone() {
		return ownerTelephone;
	}
	public void setOwnerTelephone(String ownerTelephone) {
		this.ownerTelephone = ownerTelephone;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.Long getTxSequence() {
		return txSequence;
	}
	public void setTxSequence(java.lang.Long txSequence) {
		this.txSequence = txSequence;
	}
	public java.lang.Byte getTxType() {
		return txType;
	}
	public void setTxType(java.lang.Byte txType) {
		this.txType = txType;
	}
	public java.lang.Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(java.lang.Long ownerId) {
		this.ownerId = ownerId;
	}
	public java.lang.Long getOwnerAccountId() {
		return ownerAccountId;
	}
	public void setOwnerAccountId(java.lang.Long ownerAccountId) {
		this.ownerAccountId = ownerAccountId;
	}
	public java.lang.Byte getTargetAccountType() {
		return targetAccountType;
	}
	public void setTargetAccountType(java.lang.Byte targetAccountType) {
		this.targetAccountType = targetAccountType;
	}
	public java.lang.Long getTargetAccountId() {
		return targetAccountId;
	}
	public void setTargetAccountId(java.lang.Long targetAccountId) {
		this.targetAccountId = targetAccountId;
	}
	public java.lang.Byte getBillType() {
		return billType;
	}
	public void setBillType(java.lang.Byte billType) {
		this.billType = billType;
	}
	public java.lang.Long getBillId() {
		return billId;
	}
	public void setBillId(java.lang.Long billId) {
		this.billId = billId;
	}
	public java.math.BigDecimal getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(java.math.BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	public java.lang.String getDescription() {
		return description;
	}
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	public java.lang.String getVendor() {
		return vendor;
	}
	public void setVendor(java.lang.String vendor) {
		this.vendor = vendor;
	}
	public java.lang.String getResultCodeScope() {
		return resultCodeScope;
	}
	public void setResultCodeScope(java.lang.String resultCodeScope) {
		this.resultCodeScope = resultCodeScope;
	}
	public java.lang.Integer getResultCodeId() {
		return resultCodeId;
	}
	public void setResultCodeId(java.lang.Integer resultCodeId) {
		this.resultCodeId = resultCodeId;
	}
	public java.lang.String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(java.lang.String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public java.lang.Long getOperatorUid() {
		return operatorUid;
	}
	public void setOperatorUid(java.lang.Long operatorUid) {
		this.operatorUid = operatorUid;
	}
	public java.lang.Byte getPaidType() {
		return paidType;
	}
	public void setPaidType(java.lang.Byte paidType) {
		this.paidType = paidType;
	}
	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

}
