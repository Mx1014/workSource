package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>departmentts：部门信息，参考{@link com.everhomes.rest.organization.DepartmentDTO}</li>
 * </ul>
 */
public class ListDepartmentsCommandResponse {

	private Integer nextPageOffset;
	
	@ItemType(DepartmentDTO.class)
	private List<DepartmentDTO> departments;
	
	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<DepartmentDTO> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DepartmentDTO> departments) {
		this.departments = departments;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
