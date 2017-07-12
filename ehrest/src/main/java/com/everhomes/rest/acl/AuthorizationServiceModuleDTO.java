package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.io.Serializable;
import java.util.List;

/**
 * <ul>
 * <li>allModuleFlag: 是否全部业务模块 1：是 0：否</li>
 * <li>serviceModules: 业务模块，参考{@link com.everhomes.rest.acl.ServiceModuleDTO} </li>
 * <li>resourceType:  资源类型 小区园区类型：EhCommunities</li>
 * <li>resourceId:  资源id</li>
 * <li>resourceName:  资源名称</li>
 * </ul>
 */
public class AuthorizationServiceModuleDTO implements Serializable{

	private Byte allModuleFlag;

	@ItemType(ServiceModuleAssignmentDTO.class)
	private List<ServiceModuleAssignmentDTO> assignments;

	private String resourceType;

	private Long resourceId;

	private String resourceName;

	public Byte getAllModuleFlag() {
		return allModuleFlag;
	}

	public void setAllModuleFlag(Byte allModuleFlag) {
		this.allModuleFlag = allModuleFlag;
	}

	public List<ServiceModuleAssignmentDTO> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<ServiceModuleAssignmentDTO> assignments) {
		this.assignments = assignments;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
