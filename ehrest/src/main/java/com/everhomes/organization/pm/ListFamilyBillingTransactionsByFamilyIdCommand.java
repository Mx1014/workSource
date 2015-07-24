package com.everhomes.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>pageOffset : 页码</li>
 * <li>pageSize : 页大小</li>
 * <li>familyId : 家庭Id</li>
 *</ul>
 *
 */
public class ListFamilyBillingTransactionsByFamilyIdCommand {
	
	private Long pageOffset;
	
	private Integer pageSize;
	
	@NotNull
	private Long familyId;
	
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

		public Long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
