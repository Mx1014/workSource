package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型  参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司</li>
 * <li>targetType:对象类型，参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>targetId:对象Id</li>
 * <li>moduleId:模块Id，不传查询全部</li>
 * </ul>
 */
public class ListServiceModulePrivilegesByTargetCommand {
	
	private String ownerType;

	private Long ownerId;

	private Long moduleId;

	private String targetType;

	private Long targetId;

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

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
