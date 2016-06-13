package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;



/**
 * <ul>
 * <li>pageOffset : 页码</li>
 *	<li>pageSize : 页大小</li>
 *	<li>billDate : 优先显示该月份的账单</li>
 *	<li>familyId : 家庭Id</li>
 *	<li>communityId : 小区id</li>
 *</ul>
 *
 */

public class ListFamilyBillsAndPaysByFamilyIdCommand {
	
	private Long pageOffset;
	private Integer pageSize;
	
	private String billDate;
	
	private Long familyId;
	
	@NotNull
	private Long communityId;
	
	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
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
	
	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	
	

}
