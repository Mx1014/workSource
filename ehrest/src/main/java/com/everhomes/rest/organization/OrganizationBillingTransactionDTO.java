package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *	<li>createTime : 缴费时间</li>
 *	<li>address : 楼栋门牌号</li>
 *	<li>ownerTelephone : 业主电话</li>
 *	<li>chargeAmount : 实付金额</li>
 *	<li>description : 缴费说明</li>
 *</ul>
 *
 */
public class OrganizationBillingTransactionDTO {
	
	private java.lang.Long       id;
	private java.lang.Byte       txType;
	private java.math.BigDecimal chargeAmount;
	private java.lang.String     description;
	private java.lang.String     vendor;
	private java.lang.Byte       paidType;
	private java.lang.Long   createTime;
	private String ownerTelephone;
	
	private Long organizationId;
	
	//expand
	private Long addressId;
	private String address;
	
	
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public String getOwnerTelephone() {
		return ownerTelephone;
	}
	public void setOwnerTelephone(String ownerTelephone) {
		this.ownerTelephone = ownerTelephone;
	}
	public Long getAddressId() {
		return addressId;
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
	public java.lang.Byte getTxType() {
		return txType;
	}
	public void setTxType(java.lang.Byte txType) {
		this.txType = txType;
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
	public java.lang.Byte getPaidType() {
		return paidType;
	}
	public void setPaidType(java.lang.Byte paidType) {
		this.paidType = paidType;
	}
	public java.lang.Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.lang.Long createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

}
