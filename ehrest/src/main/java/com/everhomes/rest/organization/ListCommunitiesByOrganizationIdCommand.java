// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：政府机构id</li>
 * </ul>
 */
public class ListCommunitiesByOrganizationIdCommand {
	@NotNull
	private Long    organizationId;
	
	public ListCommunitiesByOrganizationIdCommand() {
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
