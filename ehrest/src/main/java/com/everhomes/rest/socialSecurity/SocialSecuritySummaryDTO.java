package com.everhomes.rest.socialSecurity;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>社保/公积金 详细信息:
 * <li>payMonth: 月份</li>
 * <li>companyPayment: 企业缴纳</li>
 * <li>employeePayment: 个人缴纳</li>
 * <li>creatorUid: 创建人</li>
 * <li>createTime: 创建时间</li>
 * <li>fileUid: 归档人</li>
 * <li>fileTime: 归档时间</li> 
 * </ul>
 */
public class SocialSecuritySummaryDTO {
	private Long id;
    private Integer namespaceId;
    private Long organizationId;
    private String payMonth;
    private BigDecimal companyPayment;
    private BigDecimal employeePayment;
    private Long creatorUid;
    private Long createTime;
    private Long fileUid;
    private Long fileTime;

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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public BigDecimal getCompanyPayment() {
		return companyPayment;
	}

	public void setCompanyPayment(BigDecimal companyPayment) {
		this.companyPayment = companyPayment;
	}

	public BigDecimal getEmployeePayment() {
		return employeePayment;
	}

	public void setEmployeePayment(BigDecimal employeePayment) {
		this.employeePayment = employeePayment;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getFileUid() {
		return fileUid;
	}

	public void setFileUid(Long fileUid) {
		this.fileUid = fileUid;
	}

	public Long getFileTime() {
		return fileTime;
	}

	public void setFileTime(Long fileTime) {
		this.fileTime = fileTime;
	}
	
}
