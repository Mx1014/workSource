package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>rentalBillId: 订单编号 </li> 
 * <li>refundOrderNo:  退款单号</li> 
* <li>status：订单状态 •REFUNDING(9): 退款中 •REFUNDED(10): 已退款 参考{@link com.everhomes.rest.rentalv2.SiteBillStatus}</li>   
 * <li>luanchPadItemId:  来源-广场</li>  
 * <li>amount:  退款金额</li> 
 * <li>vendorType：支付方式,10001-支付宝，10002-微信</li> 
 * <li>applyUserName: 申请用户 </li> 
 * <li>applyUserContact:  联系电话</li> 
 * <li>applyTime:  申请时间</li>  
 * </ul>
 */
public class RefundOrderDTO {

	private java.lang.Long     id;
	private java.lang.String     rentalBillId;
	private java.lang.Long       refundOrderNo;
	private java.lang.Long       resourceTypeId;
	private java.math.BigDecimal amount;
	private java.lang.String     vendorType;
	private java.lang.String     useDetail;
	private Double rentalCount; 
	private java.lang.Byte     status;
	private java.lang.String     applyUserName;
	private java.lang.String     applyUserContact;
	private Long applyTime;

	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}
 
	public java.lang.Long getRefundOrderNo() {
		return refundOrderNo;
	}

	public void setRefundOrderNo(java.lang.Long refundOrderNo) {
		this.refundOrderNo = refundOrderNo;
	}

	public java.lang.Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(java.lang.Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public java.math.BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}

	public java.lang.String getVendorType() {
		return vendorType;
	}

	public void setVendorType(java.lang.String vendorType) {
		this.vendorType = vendorType;
	}

	public java.lang.Byte getStatus() {
		return status;
	}

	public void setStatus(java.lang.Byte status) {
		this.status = status;
	}

	public java.lang.String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(java.lang.String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public java.lang.String getApplyUserContact() {
		return applyUserContact;
	}

	public void setApplyUserContact(java.lang.String applyUserContact) {
		this.applyUserContact = applyUserContact;
	}

	public Long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Long applyTime) {
		this.applyTime = applyTime;
	}

	public java.lang.String getUseDetail() {
		return useDetail;
	}

	public void setUseDetail(java.lang.String useDetail) {
		this.useDetail = useDetail;
	}

	public Double getRentalCount() {
		return rentalCount;
	}

	public void setRentalCount(Double rentalCount) {
		this.rentalCount = rentalCount;
	}

	public java.lang.String getRentalBillId() {
		return rentalBillId;
	}

	public void setRentalBillId(java.lang.String rentalBillId) {
		this.rentalBillId = rentalBillId;
	}
 
	
}
