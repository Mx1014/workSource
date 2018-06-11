package com.everhomes.rest.servicehotline;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * <ul> 
 *  
 * <li>hotlines: 热线列表{@link UpdateHotlineCommand}</li>   
 * <li>serviceType: 热线的serviceType</li>
 * <li>namespaceId: 域空间</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id</li>
 * <li>appId: 应用id</li
 * </ul>
 */
public class UpdateHotlinesCommand {
	@ItemType(UpdateHotlineCommand.class)
	private List<UpdateHotlineCommand> hotlines;
	
	private Byte  serviceType;
	private Integer namespaceId;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;

	public List<UpdateHotlineCommand> getHotlines() {
		return hotlines;
	}

	public void setHotlines(List<UpdateHotlineCommand> hotlines) {
		this.hotlines = hotlines;
	}

	public Byte getServiceType() {
		return serviceType;
	}

	public void setServiceType(Byte serviceType) {
		this.serviceType = serviceType;
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
