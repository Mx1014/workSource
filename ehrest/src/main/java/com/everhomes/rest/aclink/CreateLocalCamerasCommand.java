// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name：设备名称</li>
 * <li>ipAddress：设备IP</li>
 * <li>serverId：关联服务器</li>
 * <li>doorAccessId关联门襟</li>
 * <li>enterStatus：进出标识</li>
 * <li>keyCode:摄像头密钥</li>
 * </ul>
 */
public class CreateLocalCamerasCommand {

	private Long id;
	private String name;
	private String ipAddress;
	private Long serverId;
	private Long doorAccessId;
	private Byte enterStatus;
	private String keyCode;
	private String account;
  
	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
