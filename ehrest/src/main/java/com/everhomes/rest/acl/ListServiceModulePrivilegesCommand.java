package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型  参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司</li>
 * <li>moduleId:模块Id，不传查询全部</li>
 * <li>types:需要的模块类型，不传就查询左邻运营后台的全部模块，即园区模块和管理模块， 参考{@link com.everhomes.rest.module.ServiceModuleType}</li>
 * </ul>
 */
public class ListServiceModulePrivilegesCommand {
	
	private String ownerType;

	private Long ownerId;

	private Long moduleId;

	@ItemType(Byte.class)
	private List<Byte> types;

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

	public List<Byte> getTypes() {
		return types;
	}

	public void setTypes(List<Byte> types) {
		this.types = types;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
