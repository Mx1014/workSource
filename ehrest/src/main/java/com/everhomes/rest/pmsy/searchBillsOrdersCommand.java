package com.everhomes.rest.pmsy;

/**
 *<ul>
 * <li>startDate : 开始时间</li>
 * <li>endDate : 结束时间</li>
 * <li>userName : 用户名</li>
 * <li>mobile : 手机号</li>
 *</ul>
 *
 */
public class searchBillsOrdersCommand {
	private Long startDate;
	private Long endDate;
	private String userName;
	private String mobile;
	
	public Long getStartDate() {
		return startDate;
	}
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}
	public Long getEndDate() {
		return endDate;
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
