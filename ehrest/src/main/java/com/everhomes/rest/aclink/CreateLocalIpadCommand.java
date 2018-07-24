// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name：设备名称</li>
 * <li>uuid：配对码</li>
 * <li>serverId：关联服务器id</li>
 * <li>doorAccessId：关联门禁id</li>
 * <li>enterStatus：进出标识</li>
 * </ul>
 */
public class CreateLocalIpadCommand {
	private Long id;
	private String name;
	private String uuid;
	private Long serverId;
	private Long doorAccessId;
	private Byte enterStatus;
	
	
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


	public Long getServerId() {
		return serverId;
	}


	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}


	public Long getDoorAccessId() {
		return doorAccessId;
	}


	public void setDoorAccessId(Long doorAccessId) {
		this.doorAccessId = doorAccessId;
	}


	public Byte getEnterStatus() {
		return enterStatus;
	}


	public void setEnterStatus(Byte enterStatus) {
		this.enterStatus = enterStatus;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
