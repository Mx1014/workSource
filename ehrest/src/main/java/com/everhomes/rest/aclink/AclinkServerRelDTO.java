// @formatter:off
package com.everhomes.rest.aclink;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：设备id</li>
 * <li>serverId:服务器id</li>
 * <li>doorAcessId：门禁id</li>
 * <li>deviceName：设备名称</li>
 * <li>doorAccessName：门禁名称</li>
 * <li>enterStatus：进出标识 0出1进</li>
 * <li>deviceType：设备类型 0门禁 1ipad 2摄像头</li>
 * <li>linkStatus：联网状态 0未联网 1已联网</li>
 * <li>serverIP：服务器ip</li>
 * <li>activeStatus：激活状态 0未激活 1已激活</li>
 * <li>activeDate：激活时间</li>
 * <li>version：版本号</li>
 * </ul>
 */
public class AclinkServerRelDTO {
	private Long Id;
	private Long serverId;
	private Long doorAcessId;
	private String deviceName;
	private String doorAccessName;
	private Byte enterStatus;
	private Byte deviceType;
	private Byte linkStatus;
	private String serverIP;
	private Byte activeStatus;
	private Timestamp activeDate;
	private String version;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public Long getDoorAcessId() {
		return doorAcessId;
	}

	public void setDoorAcessId(Long doorAcessId) {
		this.doorAcessId = doorAcessId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDoorAccessName() {
		return doorAccessName;
	}

	public void setDoorAccessName(String doorAccessName) {
		this.doorAccessName = doorAccessName;
	}

	public Byte getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Byte deviceType) {
		this.deviceType = deviceType;
	}

	public Byte getEnterStatus() {
		return enterStatus;
	}

	public void setEnterStatus(Byte enterStatus) {
		this.enterStatus = enterStatus;
	}

	public Byte getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(Byte linkStatus) {
		this.linkStatus = linkStatus;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public Byte getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Byte activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Timestamp getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Timestamp activeDate) {
		this.activeDate = activeDate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
