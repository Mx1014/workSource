package com.everhomes.rest.acl;



/**
 * <p>项目/p>
 * <ul>
 * <li>projectId: 项目id</li>
 * <li>projectType: 项目类型</li>
 * </ul>
 */
public class ProjectDTO {
	
	private Long    projectId;
	private String  projectType;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
}