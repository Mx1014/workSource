package com.everhomes.rest.socialSecurity;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>社保/公积金 详细信息:
 * <li>detailId: detailId</li>
 * <li>userName: 姓名</li>
 * <li>deptName: 部门名</li>
 * <li>entryDate: 入职日期</li>
 * <li>outDate: 离职日期</li>
 * <li>socialSecurityCity: 社保城市</li>
 * <li>accumulationFundCity: 公积金城市</li>
 * <li>socialSecurityRadix: 社保基数</li>
 * <li>accumulationFundRadix: 公积金基数</li>
 * </ul>
 */
public class SocialSecurityPaymentDTO {
	private Long id;
	private Long detailId;
	private String userName;
	private String deptName;
	private Long entryDate;
	private Long outDate;
	private String socialSecurityCity;
	private String accumulationFundCity;
	private BigDecimal socialSecurityRadix;
	private BigDecimal accumulationFundRadix;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Long getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Long entryDate) {
		this.entryDate = entryDate;
	}

	public Long getOutDate() {
		return outDate;
	}

	public void setOutDate(Long outDate) {
		this.outDate = outDate;
	}

	public String getSocialSecurityCity() {
		return socialSecurityCity;
	}

	public void setSocialSecurityCity(String socialSecurityCity) {
		this.socialSecurityCity = socialSecurityCity;
	}

	public String getAccumulationFundCity() {
		return accumulationFundCity;
	}

	public void setAccumulationFundCity(String accumulationFundCity) {
		this.accumulationFundCity = accumulationFundCity;
	}

	public BigDecimal getSocialSecurityRadix() {
		return socialSecurityRadix;
	}

	public void setSocialSecurityRadix(BigDecimal socialSecurityRadix) {
		this.socialSecurityRadix = socialSecurityRadix;
	}

	public BigDecimal getAccumulationFundRadix() {
		return accumulationFundRadix;
	}

	public void setAccumulationFundRadix(BigDecimal accumulationFundRadix) {
		this.accumulationFundRadix = accumulationFundRadix;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
}
