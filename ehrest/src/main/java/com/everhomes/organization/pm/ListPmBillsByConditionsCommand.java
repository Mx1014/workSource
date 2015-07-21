package com.everhomes.organization.pm;

import java.sql.Date;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
  <ul>
  	<li>pageOffset : 下一条记录id</li>	
	<li>pageSize : 页大小</li>	
	
	<li>organizationId : 组织Id</li>
	
	<li>billDate : 账单日期</li>
	<li>address : 地址</li>
  </ul>
 *
 */

public class ListPmBillsByConditionsCommand {
	
	private Long pageOffset;
	private Integer pageSize;
	
	@NotNull
	private Long organizationId;
	
	private String address;
	private String billDate;
	
	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public Long getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Long pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
