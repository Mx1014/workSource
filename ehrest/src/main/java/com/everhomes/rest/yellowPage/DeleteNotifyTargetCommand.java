package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerType: 拥有者类型：现在是comunity</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>id: 主键id</li>
 *  <li>appId: 校验权限时的应用id</li>
 *  <li>currentPMId: 校验权限时的当前管理公司id</li>
 * </ul>
 */
public class DeleteNotifyTargetCommand {

	@NotNull
	private Long id;
	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;
	@NotNull
	private Long appId;
	@NotNull
	private Long currentPMId;
	
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

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}
}
