// @formatter:off
package com.everhomes.department;

import java.util.List;

import com.everhomes.discover.ItemType;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：机构成员信息，参考{@link com.everhomes.department.DepartmentMemberDTO}</li>
 * </ul>
 */
public class ListDptMemberCommandResponse {
	
	private Integer nextPageOffset;
	
	@ItemType(DepartmentMemberDTO.class)
    private List<DepartmentMemberDTO> members;
	public ListDptMemberCommandResponse() {
    }
	
	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<DepartmentMemberDTO> getMembers() {
		return members;
	}

	public void setMembers(List<DepartmentMemberDTO> members) {
		this.members = members;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
