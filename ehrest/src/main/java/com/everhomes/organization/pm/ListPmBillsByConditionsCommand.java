package com.everhomes.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
  <ul>
  	<li>pageOffset : 下一条记录id</li>	
	<li>pageSize : 页大小</li>	
	<li>address : 地址</li>
	<li>dateStr : 账单日期</li>
	<li>communityId : 小区Id</li>
  </ul>
 *
 */

public class ListPmBillsByConditionsCommand {
	
	private Long pageOffset;
	
	private Integer pageSize;
	
	private String address;
	
	private String dateStr;
	
	@NotNull
	private Long communityId;
	
	
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

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

}
