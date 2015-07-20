package com.everhomes.organization.pm;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>pageOffSet : 页码</li>
 *	<li>pageSize : 页大小</li>
 *	<li>address : 地址</li>
 *	<li>createTime : 缴费日期</li>
 *	<li>communityId : 小区Id</li>
 *</ul>
 *
 */
public class ListOrgBillingTransactionsByConditionCommand {
	
	private Long pageOffset;
	private Integer pageSize;
	
	private String address;
	private String createTime;
	
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
