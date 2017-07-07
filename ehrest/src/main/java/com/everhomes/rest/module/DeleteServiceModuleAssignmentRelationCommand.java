package com.everhomes.rest.module;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 业务模块关系id</li>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型  参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司</li>
 * </ul>
 */
public class DeleteServiceModuleAssignmentRelationCommand {

	private Long id;

	private String ownerType;

	private Long ownerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
