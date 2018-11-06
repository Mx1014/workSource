package com.everhomes.rest.news;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>categoryId : 新闻类型id</li>
 * <li>projectType : EhCommunities-项目配置 / organization-通用配置 {@link com.everhomes.rest.news.NewsOwnerType}</li>
 * <li>projectId : 园区id/公司id</li>
 * <li>currentPMId : 管理公司id</li>
 * <li>appId : 应用的originId</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年8月22日
 */
public class GetSelfDefinedStateCommand {
	
	private Long categoryId;
	private String projectType;
	private Long projectId;
	private Long currentPMId;
	private Long appId;
	

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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	
}
