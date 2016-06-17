package com.everhomes.rest.payment;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>cardId: 卡ID</li>
 * <li>mobile: 手机号</li>
 * <li>cardNo: 卡号（用于显示的）</li>
 * <li>cardType: 卡类型</li>
 * <li>activedTime: 激活时间</li>
 * <li>expiredTime: 有效时间</li>
 * <li>balance: 余额</li>
 * <li>status: 卡状态</li>
 * </ul>
 */
public class CardInfoDTO {
	private Long cardId;
	private String mobile;
	private String cardNo;
	private String cardType;
	private Timestamp activedTime;
	private Timestamp expiredTime;
	private BigDecimal balance;
	private String status;
	private String vendorCardData;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Timestamp getActivedTime() {
		return activedTime;
	}
	public void setActivedTime(Timestamp activedTime) {
		this.activedTime = activedTime;
	}
	public Timestamp getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(Timestamp expiredTime) {
		this.expiredTime = expiredTime;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Long getCardId() {
		return cardId;
	}
	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
	
	public String getVendorCardData() {
		return vendorCardData;
	}
	public void setVendorCardData(String vendorCardData) {
		this.vendorCardData = vendorCardData;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
