package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 当前机构id</li>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型, 参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司, 如果是左邻后台传0即可</li>
 * <li>targetId：对象id</li>
 * <li>targetType：对象类型，EhUsers, EhOrganizations,{@link com.everhomes.rest.common.EntityType}</li>
 * <li>allFlag：是否全部业务模块 1：是 0：否，{@link com.everhomes.rest.common.AllFlagType}</li>
 * <li>moduleIds:  业务模块id集合</li>
 * </ul>
 */
public class CreateServiceModuleAdministratorsCommand {

	@NotNull
	private Long organizationId;

	@NotNull
	private String ownerType;

	@NotNull
	private Long ownerId;

	private Long targetId;

	private String targetType;

	private Byte allFlag;

	@NotNull
	@ItemType(Long.class)
	private List<Long> moduleIds;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Byte getAllFlag() {
		return allFlag;
	}

	public void setAllFlag(Byte allFlag) {
		this.allFlag = allFlag;
	}

	public List<Long> getModuleIds() {
		return moduleIds;
	}

	public void setModuleIds(List<Long> moduleIds) {
		this.moduleIds = moduleIds;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
