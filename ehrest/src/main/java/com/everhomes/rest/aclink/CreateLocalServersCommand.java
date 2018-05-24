// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name：内网服务器名称</li>
 * <li>uuid：服务器配对码</li>
 * <li>ownerId：所属机构id</li>
 * <li>ownerType:所属上级类型 0小区 1企业 2家庭{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * </ul>
 */
public class CreateLocalServersCommand {
	private Long id;
	private String name;
	private String uuid;
	private Long ownerId;
	private Byte ownerType;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
    public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Byte getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
