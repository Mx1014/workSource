package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>type : 服务联盟类型id</li>
 * <li>projectType : community-项目配置 / organaization-通用配置 注意organaization拼写，历史遗留问题，中间是nai</li>
 * <li>projectId : 园区id/公司id</li>
 * <li>currentPMId : 管理公司id</li>
 * <li>appId : 应用的originId</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年8月22日
 */
public class GetSelfDefinedStateCommand {
	private Long type;
	private String projectType;
	private Long projectId;
	private Long currentPMId;
	private Long appId;
	
	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	
}
