package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>type : 服务联盟类型id</li>
 * <li>projectId : 需要设置的项目id</li>
 * <li>currentPMId : 管理公司id</li>
 * <li>appId : 应用的originId</li>
 * <li>isOpen : 1-开启 0-关闭</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年8月22日
 */
public class UpdateProjectConfigFlagCommand {
	
	private Long type;
	private Long projectId;
	private Long currentPMId;
	private Long appId;
	private Byte isOpen;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	} 
	
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Long getCurrentPMId() {
		return currentPMId;
	}
	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Byte getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Byte isOpen) {
		this.isOpen = isOpen;
	}
}
