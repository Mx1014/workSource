// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：机构Id</li>
 * <li>tierNum：层数</li>
 * </ul>
 */
public class ListAllTreeOrganizationsCommand {

	private Long organizationId;

	private Integer tierNum;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getTierNum() {
		return tierNum;
	}

	public void setTierNum(Integer tierNum) {
		this.tierNum = tierNum;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
