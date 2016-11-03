package com.everhomes.rest.acl.admin;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>allModuleFlag: 是否全部业务模块 1：是 0：否</li>
 * <li>moduleIds: 业务模块集</li>
 * <li>resourceType:  资源类型 小区园区类型：EhCommunities</li>
 * <li>resourceId:  资源id</li>
 * </ul>
 */
public class AuthorizationServiceModule {

	@NotNull
	private Byte allModuleFlag;

	@ItemType(Long.class)
	private List<Long> moduleIds;

	@NotNull
	private String resourceType;

	@NotNull
	private Long resourceId;

	public Byte getAllModuleFlag() {
		return allModuleFlag;
	}

	public void setAllModuleFlag(Byte allModuleFlag) {
		this.allModuleFlag = allModuleFlag;
	}

	public List<Long> getModuleIds() {
		return moduleIds;
	}

	public void setModuleIds(List<Long> moduleIds) {
		this.moduleIds = moduleIds;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
