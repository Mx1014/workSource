package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 机构id </li>
 * <li>name: 机构名称 </li>
 * <li>parentId : 父级id</li>
 * <li>parentName: 父级名称</li>
 * <li>path：路径，含层次关系，如/父亲id/第一层孩子id/第二层孩子id/...</li>
 * <li>jobPositions : 通用岗位列表 参考{@link com.everhomes.rest.organization.OrganizationJobPositionDTO}</li>
 * </ul>
 */
public class ChildrenOrganizationJobPositionDTO {

	private Long    id;
	private Long    parentId;
	private String parentName;
	private String  name;
	private String  path;
	@ItemType(OrganizationMemberDTO.class)
	private List<OrganizationMemberDTO> members;
	@ItemType(OrganizationJobPositionDTO.class)
	private List<OrganizationJobPositionDTO> jobPositions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<OrganizationJobPositionDTO> getJobPositions() {
		return jobPositions;
	}

	public void setJobPositions(List<OrganizationJobPositionDTO> jobPositions) {
		this.jobPositions = jobPositions;
	}

	public List<OrganizationMemberDTO> getMembers() {
		return members;
	}

	public void setMembers(List<OrganizationMemberDTO> members) {
		this.members = members;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
