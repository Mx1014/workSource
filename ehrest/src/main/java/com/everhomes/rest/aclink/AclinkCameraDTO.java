// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
import java.sql.Timestamp;

/**
 * <ul>
 * <li>id：内网摄像头 id</li>
 * <li>name：内网摄像头名称</li>
 * <li>doorAccess: 关联门禁{@link com.everhomes.rest.aclink.DoorAccessDTO}</li>
 * <li>enterStatus:进出标识 1进0出{@link com.everhomes.rest.aclink.AclinkEnterStatus}</li>
 * <li>linkStatus：联网状态{@link com.everhomes.rest.aclink.DoorAccessLinkStatus}</li>
 * <li>ipAddress：ip地址</li>
 * <li>server：关联服务器 {@link com.everhomes.rest.aclink.AclinkServerDTO}</li>
 * <li>createTime：创建时间</li>
 * <li>status:0未激活 1已激活 2已删除</li>
 * <li>keyCode:摄像头密钥</li>
 * </ul>
 */
public class AclinkCameraDTO {
	private Long id;
	private String name;
	private DoorAccessDTO doorAccess;
	private Long doorAccessId;
	private Byte enterStatus;
	private Byte linkStatus;
	private String ipAddress;
	private AclinkServerDTO server;
	private Timestamp createTime;
	private Byte status;
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

	public DoorAccessDTO getDoorAccess() {
		return doorAccess;
	}

	public void setDoorAccess(DoorAccessDTO doorAccess) {
		this.doorAccess = doorAccess;
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public AclinkServerDTO getServer() {
		return server;
	}

	public void setServer(AclinkServerDTO server) {
		this.server = server;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getDoorAccessId() {
		return doorAccessId;
	}

	public void setDoorAccessId(Long doorAccessId) {
		this.doorAccessId = doorAccessId;
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
