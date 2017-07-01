// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>departmentId: 部门id</li>
 * <li>userId: 用户id</li>
 * <li>salaryGroupId: 薪酬组id</li>
 * <li>ownerType: 'organization'</li>
 * <li>ownerId: organizationId</li>
 * </ul>
 */
public class AddToOrganizationSalaryGroupCommand {

	@ItemType(Long.class)
	private List<Long> departmentId;

	@ItemType(Long.class)
	private List<Long> userId;

	private Long salaryGroupId;

	private String ownerType;

	private Long ownerId;

	public AddToOrganizationSalaryGroupCommand() {

	}

	public List<Long> getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(List<Long> departmentId) {
		this.departmentId = departmentId;
	}

    public Long getSalaryGroupId() {
        return salaryGroupId;
    }

    public void setSalaryGroupId(Long salaryGroupId) {
        this.salaryGroupId = salaryGroupId;
    }

    public List<Long> getUserId() {
		return userId;
	}

	public void setUserId(List<Long> userId) {
		this.userId = userId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
