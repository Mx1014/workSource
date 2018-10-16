package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>mobile: 手机号</li>
 * <li>userName: 用户名称</li>
 * <li>cardNo: 卡号</li>
 * <li>amount: 金额</li>
 * <li>rechargeTime: 充值时间</li>
 * <li>rechargeStatus:  0:充值失败   1:处理中  2:充值成功 3:处理完成  4:退款中 5:已退款{@link com.everhomes.rest.payment.CardRechargeStatus}</li>
 * <li>paidType: 支付方式 10001-支付宝，10002-微信</li>
 * <li>nextPageAnchor: 分页的瞄 </li>
 * </ul>
 */
public class CardRechargeOrderDTO {
	private java.lang.Long       id;
	private java.lang.String     userName;
	private java.lang.String     mobile;
	private java.lang.String     cardNo;
	private java.math.BigDecimal amount;
	private java.sql.Timestamp   rechargeTime;
	private java.lang.Byte       rechargeStatus;
	private java.lang.String     paidType;
    private Long nextPageAnchor;
	
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.String getUserName() {
		return userName;
	}
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}
	public java.lang.String getMobile() {
		return mobile;
	}
	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}
	public java.lang.String getCardNo() {
		return cardNo;
	}
	public void setCardNo(java.lang.String cardNo) {
		this.cardNo = cardNo;
	}
	public java.math.BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}
	public java.sql.Timestamp getRechargeTime() {
		return rechargeTime;
	}
	public void setRechargeTime(java.sql.Timestamp rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
	public java.lang.Byte getRechargeStatus() {
		return rechargeStatus;
	}
	public void setRechargeStatus(java.lang.Byte rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}
	public java.lang.String getPaidType() {
		return paidType;
	}
	public void setPaidType(java.lang.String paidType) {
		this.paidType = paidType;
	}
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
