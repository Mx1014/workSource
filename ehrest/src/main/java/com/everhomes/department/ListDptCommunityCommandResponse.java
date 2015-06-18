// @formatter:off
package com.everhomes.department;

import java.util.List;

import com.everhomes.discover.ItemType;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：机构对应的小区信息，参考{@link com.everhomes.department.DepartmentCommunityDTO}</li>
 * </ul>
 */
public class ListDptCommunityCommandResponse {
	
	private Integer nextPageOffset;
	
	@ItemType(DepartmentCommunityDTO.class)
    private List<DepartmentCommunityDTO> members;
	public ListDptCommunityCommandResponse() {
    }
	
	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<DepartmentCommunityDTO> getMembers() {
		return members;
	}

	public void setMembers(List<DepartmentCommunityDTO> members) {
		this.members = members;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
