package com.everhomes.rest.module;



/**
 * <p>项目/p>
 * <ul>
 * <li>projectId: 项目id</li>
 * <li>projectType: 项目类型 </li>
 * <li>projectName: 项目名称 </li>
 * </ul>
 */
public class Project {

	private Long    projectId;
	private String  projectType;
	private String projectName;

	public Project(){

	}

	public Project(String projectType, Long projectId){
		this.projectId = projectId;
		this.projectType = projectType;
	}

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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}