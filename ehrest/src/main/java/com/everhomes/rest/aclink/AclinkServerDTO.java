// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;
import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id：内网服务器id</li>
 * <li>name：内网服务器名称</li>
 * <li>namespaceId:域空间id</li>
 * <li>uuid：服务器配对码</li>
 * <li>ipAddress: ip地址</li>
 * <li>linkStatus：联网状态 0成功 1失败{@link com.everhomes.rest.aclink.DoorAccessLinkStatus}</li>
 * <li>activeTime：激活时间</li>
 * <li>createTime:创建时间</li>
 * <li>syncTime：上次同步时间</li>
 * <li>version：版本号</li>
 * <li>ownerId:所属机构Id</li>
 * <li>ownerName：所属机构名称</li>
 * <li>ownerType：所属机构类型 0小区 1企业 2家庭{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>status:0未激活 1已激活 2已删除</li>
 * <li>listDoorAccess:服务器下属门禁{@link com.everhomes.rest.aclink.LocalDoorAccessDTO}</li>
 * </ul>
 */
public class AclinkServerDTO {
	private Long id;
	private String name;
	private Integer namespaceId;
	private String uuid;
	private Byte linkStatus;
	private String ipAddress;
	private Timestamp activeTime;
	private Timestamp syncTime;
	private Timestamp createTime;
	private String version;
	private Long ownerId;
	private String ownerName;
	private Byte ownerType;
	private Byte status;
	private String localServerKey;
	private List<LocalDoorAccessDTO> listDoorAccess;

	public String getUuidNum(){
		return uuid.replace(":", "");
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Timestamp getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Timestamp activeTime) {
		this.activeTime = activeTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Byte getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Timestamp getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Timestamp syncTime) {
		this.syncTime = syncTime;
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getLocalServerKey() {
		return localServerKey;
	}

	public void setLocalServerKey(String localServerKey) {
		this.localServerKey = localServerKey;
	}

	public List<LocalDoorAccessDTO> getListDoorAccess() {
		return listDoorAccess;
	}

	public void setListDoorAccess(List<LocalDoorAccessDTO> listDoorAccess) {
		this.listDoorAccess = listDoorAccess;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
