// @formatter:off
package com.everhomes.department;

import java.util.List;

import com.everhomes.discover.ItemType;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：物业账单信息，参考{@link com.everhomes.department.DepartmentDTO}</li>
 * </ul>
 */
public class ListDepartmentsCommandResponse {
	
	private Integer nextPageOffset;
	
	@ItemType(DepartmentDTO.class)
    private List<DepartmentDTO> members;
	public ListDepartmentsCommandResponse() {
    }
	
	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<DepartmentDTO> getMembers() {
		return members;
	}

	public void setMembers(List<DepartmentDTO> members) {
		this.members = members;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
