package com.everhomes.rest.investmentAd;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id: 招商广告id</li>
 *  <li>organizationId: organizationId</li>
 * </ul>
 */
public class GetRelatedAssetsCommand {
	
	@NotNull
	private Long id;
	private Long organizationId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
