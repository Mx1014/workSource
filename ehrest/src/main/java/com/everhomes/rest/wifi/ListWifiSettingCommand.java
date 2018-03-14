package com.everhomes.rest.wifi;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.wifi.WifiOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>pageAnchor: 分页的瞄</li>
 * <li>pageSize: 每页显示条数</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class ListWifiSettingCommand {
	
	private java.lang.String   ownerType;
	private java.lang.Long     ownerId;
	private Long pageAnchor;
    private Integer pageSize;
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
	
	public java.lang.String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(java.lang.String ownerType) {
		this.ownerType = ownerType;
	}
	public java.lang.Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(java.lang.Long ownerId) {
		this.ownerId = ownerId;
	}
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
