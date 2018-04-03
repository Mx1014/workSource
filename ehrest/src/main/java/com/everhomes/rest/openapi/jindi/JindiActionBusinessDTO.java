// @formatter:off
package com.everhomes.rest.openapi.jindi;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 
 * <ul>金地同步数据的电商数据
 * <li>: </li>
 * </ul>
 */
public class JindiActionBusinessDTO extends JindiDataDTO {
	private Long id;
	private Long userId;
	private String userName;
	private String phone;
	private Long communityId;
	private String communityName;
	private String transactionNo;
	private Timestamp paidTime;
	private Byte paidChannel;
	private BigDecimal paidAmount;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public Timestamp getPaidTime() {
		return paidTime;
	}

	public void setPaidTime(Timestamp paidTime) {
		this.paidTime = paidTime;
	}

	public Byte getPaidChannel() {
		return paidChannel;
	}

	public void setPaidChannel(Byte paidChannel) {
		this.paidChannel = paidChannel;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

}
