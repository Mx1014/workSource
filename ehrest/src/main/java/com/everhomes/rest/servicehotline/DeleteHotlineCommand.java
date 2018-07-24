package com.everhomes.rest.servicehotline;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * 
 * <li>id: id</li>  
 * <li>namespaceId: 域空间</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id</li>
 * <li>appId: 应用id</li> 
 * </ul>
 */
public class DeleteHotlineCommand {
	private Long id;  
	private Integer namespaceId;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

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
}
