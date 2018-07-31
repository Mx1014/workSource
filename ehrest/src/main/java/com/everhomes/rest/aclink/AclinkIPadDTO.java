// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
import java.sql.Timestamp;

/**
 * <ul>
 * <li>id：内网ipad id</li>
 * <li>name：内网ipad名称</li>
 * <li>uuid：ipad配对码</li>
 * <li>doorAccess: 关联门禁{@link com.everhomes.rest.aclink.DoorAccessDTO}</li>
 * <li>enterStatus:进出标识 1进0出{@link com.everhomes.rest.aclink.AclinkEnterStatus}</li>
 * <li>uuid:配对码</li>
 * <li>server：关联服务器{@link com.everhomes.rest.aclink.AclinkServerDTO}</li>
 * <li>linkStatus：联网状态{@link com.everhomes.rest.aclink.DoorAccessLinkStatus}</li>
 * <li>activeTime：激活时间</li>
 * <li>createTime:创建时间</li>
 * <li>status:0未激活 1已激活 2已删除</li>
 * </ul>
 */
public class AclinkIPadDTO {
	private Long id;
	private String name;
	private DoorAccessDTO doorAccess;
	private Byte enterStatus;
	private String uuid;
	private AclinkServerDTO server;
	private Byte linkStatus;
	private Timestamp activeTime;
	private Timestamp createTime;
	private Byte status;
	private Long doorAccessId;

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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Byte getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(Byte linkStatus) {
		this.linkStatus = linkStatus;
	}

	public Timestamp getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Timestamp activeTime) {
		this.activeTime = activeTime;
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

	public AclinkServerDTO getServer() {
		return server;
	}

	public void setServer(AclinkServerDTO server) {
		this.server = server;
	}

	public Long getDoorAccessId() {
		return doorAccessId;
	}

	public void setDoorAccessId(Long doorAccessId) {
		this.doorAccessId = doorAccessId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
