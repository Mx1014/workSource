// @formatter:off
package com.everhomes.rest.openapi.jindi;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 
 * <ul>金地同步数据的场所预订数据
 * <li>: </li>
 * </ul>
 */
public class JindiActionSiteRentalDTO extends JindiDataDTO {
	private Long id;
	private Long userId;
	private String userName;
	private Long communityId;
	private String communityName;
	private String phone;
	private Timestamp startTime;
	private Timestamp endTime;
	private String orderNo;
	private String vendorType;
	private Timestamp reserveTime;
	private BigDecimal payTotalMoney;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getVendorType() {
		return vendorType;
	}

	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	public Timestamp getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(Timestamp reserveTime) {
		this.reserveTime = reserveTime;
	}

	public BigDecimal getPayTotalMoney() {
		return payTotalMoney;
	}

	public void setPayTotalMoney(BigDecimal payTotalMoney) {
		this.payTotalMoney = payTotalMoney;
	}

}
