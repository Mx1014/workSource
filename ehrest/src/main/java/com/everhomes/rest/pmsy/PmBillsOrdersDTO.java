package com.everhomes.rest.pmsy;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 订单ID</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.pmsy.PmsyOwnerType}</li>
 * <li>payerUid: 付款人ID</li>
 * <li>userName: 该订单 用户名称</li>
 * <li>userContact: 该订单 用户手机号</li>
 * <li>orderAmount: 该订单缴纳 金额</li>
 * <li>paidTime: 付款时间</li>
 * <li>status: 订单状态 {@link com.everhomes.rest.pmsy.PmsyOrderStatus}</li>
 * <li>creatorUid: 订单创建人ID</li>
 * <li>createTime: 缴费记录创建日期</li>
 * <li>paidType: 支付方式,10001-支付宝，10002-微信 com.everhomes.rest.organization.VendorType</li>
 * </ul>
 */
public class PmBillsOrdersDTO {
	private Long id;
	private Long ownerId;
	private String ownerType;
	private Long payerUid;
	private String userName;
	private String userContact;
	private BigDecimal orderAmount;
	private Timestamp paidTime;
	private byte status;
	private Long creatorUid;
	private Timestamp createTime;
	private String paidType;
	private Timestamp billDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getPayerUid() {
		return payerUid;
	}
	public void setPayerUid(Long payerUid) {
		this.payerUid = payerUid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserContact() {
		return userContact;
	}
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Timestamp getPaidTime() {
		return paidTime;
	}
	public void setPaidTime(Timestamp paidTime) {
		this.paidTime = paidTime;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getPaidType() {
		return paidType;
	}
	public void setPaidType(String paidType) {
		this.paidType = paidType;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Timestamp getBillDate() {
		return billDate;
	}
	public void setBillDate(Timestamp billDate) {
		this.billDate = billDate;
	}
}
