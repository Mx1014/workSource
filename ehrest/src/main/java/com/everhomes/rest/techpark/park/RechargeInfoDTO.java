package com.everhomes.rest.techpark.park;

import java.sql.Timestamp;

/**
 * <ul>
 *  <li>billId:订单号</li>
 *  <li>plateNumber:牌照号</li>
 *  <li>numberType:号码类型  0为车牌</li>
 *  <li>ownerName:车主姓名</li>
 *  <li>rechargeUserid:充值人id</li>
 *  <li>rechargeUsername:充值人姓名</li>
 *  <li>rechargePhone:充值人手机号</li>
 *  <li>rechargeTime:充值时间</li>
 *  <li>rechargeMonth:充值月份</li>
 *  <li>rechargeAmount:充值金额</li>
 *  <li>oldValidityperiod:原到期日期</li>
 *  <li>newValidityperiod:新的到期日期</li>
 *  <li>paymentStatus:支付状态  0-支付失败；1-未支付；2-支付成功</li>
 *  <li>rechargeStatus:充值状态 0-充值失败；1-等待付款；2-数据更新中；3-充值成功</li>
 *  <li>enterpriseCommunityId:园区id</li>
 * </ul>
 */

public class RechargeInfoDTO {
	
	private Long id;
	
	private Long billId;
	
	private String plateNumber;
	
	private Byte numberType;
	
	private String ownerName;
	
	private Long rechargeUserid;
	
	private String rechargeUsername;
	
	private String rechargePhone;
	
	private Timestamp rechargeTime;
	
	private Byte rechargeMonth;
	
	private Double rechargeAmount;
	
	private Timestamp oldValidityperiod;
	
	private Timestamp newValidityperiod;
	
	private Byte paymentStatus;
	
	private Byte rechargeStatus;
	
	private Long enterpriseCommunityId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public Byte getNumberType() {
		return numberType;
	}

	public void setNumberType(Byte numberType) {
		this.numberType = numberType;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Long getRechargeUserid() {
		return rechargeUserid;
	}

	public void setRechargeUserid(Long rechargeUserid) {
		this.rechargeUserid = rechargeUserid;
	}

	public String getRechargeUsername() {
		return rechargeUsername;
	}

	public void setRechargeUsername(String rechargeUsername) {
		this.rechargeUsername = rechargeUsername;
	}

	public String getRechargePhone() {
		return rechargePhone;
	}

	public void setRechargePhone(String rechargePhone) {
		this.rechargePhone = rechargePhone;
	}

	public Timestamp getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(Timestamp rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public Byte getRechargeMonth() {
		return rechargeMonth;
	}

	public void setRechargeMonth(Byte rechargeMonth) {
		this.rechargeMonth = rechargeMonth;
	}

	public Double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public Timestamp getOldValidityperiod() {
		return oldValidityperiod;
	}

	public void setOldValidityperiod(Timestamp oldValidityperiod) {
		this.oldValidityperiod = oldValidityperiod;
	}

	public Timestamp getNewValidityperiod() {
		return newValidityperiod;
	}

	public void setNewValidityperiod(Timestamp newValidityperiod) {
		this.newValidityperiod = newValidityperiod;
	}

	public Byte getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Byte paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Byte getRechargeStatus() {
		return rechargeStatus;
	}

	public void setRechargeStatus(Byte rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}

	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}

	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
	}
	
	

}
