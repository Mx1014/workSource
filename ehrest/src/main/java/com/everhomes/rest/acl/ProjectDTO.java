package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <p>项目/p>
 * <ul>
 * <li>projectId: 项目id</li>
 * <li>projectType: 项目类型 </li>
 * <li>projectName: 项目名称</li>
 * <li>parentId: 父级Id</li>
 * <li>projects: 子集，参考{@link com.everhomes.rest.acl.ProjectDTO}</li>
 * </ul>
 */
public class ProjectDTO {

	private Long parentId;
	private Long    projectId;
	private String projectName;
	private String  projectType;

	@ItemType(ProjectDTO.class)
	private List<ProjectDTO> projects;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectType() {
		return projectType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<ProjectDTO> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectDTO> projects) {
		this.projects = projects;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}