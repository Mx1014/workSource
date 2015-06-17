// @formatter:off
package com.everhomes.department;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>departmentId：机构id</li>
 * <li>communityIds：小区id列表</li>
 * </ul>
 */
public class CreateDepartmentCommunityCommand {
	@NotNull
	private Long departmentId;
	@NotNull
	private List<Long> communityIds;

	

	public Long getDepartmentId() {
		return departmentId;
	}



	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}



	public List<Long> getCommunityIds() {
		return communityIds;
	}



	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
