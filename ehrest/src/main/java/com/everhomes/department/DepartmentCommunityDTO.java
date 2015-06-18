// @formatter:off
package com.everhomes.department;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.address.CommunityDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>department：机构详情</li>
 * <li>communityList：对应小区详情</li>
 * </ul>
 */
public class DepartmentCommunityDTO {
	
	private DepartmentDTO department;
	
	@ItemType(Long.class)
	private List<CommunityDTO> communityList;

	
	public DepartmentCommunityDTO() {
		
	}
	

	public DepartmentDTO getDepartment() {
		return department;
	}


	public void setDepartment(DepartmentDTO department) {
		this.department = department;
	}


	public List<CommunityDTO> getCommunityList() {
		return communityList;
	}


	public void setCommunityList(List<CommunityDTO> communityList) {
		this.communityList = communityList;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
