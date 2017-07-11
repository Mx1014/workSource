// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.uniongroup.UniongroupTarget;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>departmentIds: 部门id</li>
 * <li>detailIds: 用户DetailId</li>
 * <li>salaryGroupId: 薪酬组id</li>
 * <li>name: 部门或人员名称</li>
 * <li>ownerType: 'organization'</li>
 * <li>ownerId: organizationId</li>
 * </ul>
 */
public class AddToOrganizationSalaryGroupCommand {

	@ItemType(UniongroupTarget.class)
	private List<UniongroupTarget> departmentIds;

	@ItemType(UniongroupTarget.class)
	private List<UniongroupTarget> detailIds;

	private Long salaryGroupId;

	private String name;

	private String ownerType;

	private Long ownerId;

	public AddToOrganizationSalaryGroupCommand() {

	}

    public List<UniongroupTarget> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<UniongroupTarget> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public List<UniongroupTarget> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<UniongroupTarget> detailIds) {
        this.detailIds = detailIds;
    }

    public Long getSalaryGroupId() {
        return salaryGroupId;
    }

    public void setSalaryGroupId(Long salaryGroupId) {
        this.salaryGroupId = salaryGroupId;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
