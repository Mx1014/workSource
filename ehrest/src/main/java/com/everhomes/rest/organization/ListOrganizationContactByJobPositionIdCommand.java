// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>jobPositionId：通用岗位id</li>
 * <li>organizationId：机构id，</li>
 * </ul>
 */
public class ListOrganizationContactByJobPositionIdCommand {

	private Long jobPositionId;

	private Long organizationId;

	public Long getJobPositionId() {
		return jobPositionId;
	}

	public void setJobPositionId(Long jobPositionId) {
		this.jobPositionId = jobPositionId;
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
