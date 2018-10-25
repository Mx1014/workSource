package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 拥有者类型：现在是comunity</li>
 * <li>ownerId: 拥有者ID</li>
 * <li>type:类型  </li>
 * <li>sourceRequestType: 0-客户端/浏览器请求  其他-后台请求 </li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class GetServiceAllianceCommand {

	private String ownerType;
	
	@NotNull
	private Long ownerId;
	
	private Long type;
	
	private Byte sourceRequestType;
	
	private Integer namespaceId;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Byte getSourceRequestType() {
		return sourceRequestType;
	}

	public void setSourceRequestType(Byte sourceRequestType) {
		this.sourceRequestType = sourceRequestType;
	}
}
