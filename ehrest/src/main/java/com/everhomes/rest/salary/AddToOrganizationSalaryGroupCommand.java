// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.uniongroup.UniongroupTarget;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>departments: 部门信息{@link com.everhomes.rest.uniongroup.UniongroupTarget}</li>
 * <li>users: 用户信息，参考{@link com.everhomes.rest.uniongroup.UniongroupTarget}</li>
 * <li>salaryGroupId: 薪酬组id</li>
 * <li>ownerType: 'organization'</li>
 * <li>ownerId: organizationId</li>
 * </ul>
 */
public class AddToOrganizationSalaryGroupCommand {

	@ItemType(UniongroupTarget.class)
	private List<UniongroupTarget> departments;

	@ItemType(UniongroupTarget.class)
	private List<UniongroupTarget> users;

	private Long salaryGroupId;

	private String ownerType;

	private Long ownerId;

	public AddToOrganizationSalaryGroupCommand() {

	}

    public List<UniongroupTarget> getDepartments() {
        return departments;
    }

    public void setDepartments(List<UniongroupTarget> departments) {
        this.departments = departments;
    }

    public List<UniongroupTarget> getUsers() {
        return users;
    }

    public void setUsers(List<UniongroupTarget> users) {
        this.users = users;
    }

    public Long getSalaryGroupId() {
        return salaryGroupId;
    }

    public void setSalaryGroupId(Long salaryGroupId) {
        this.salaryGroupId = salaryGroupId;
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
