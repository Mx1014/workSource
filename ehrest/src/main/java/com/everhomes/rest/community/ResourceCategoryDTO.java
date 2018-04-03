package com.everhomes.rest.community;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>name 类别名称 </li>
 * <li>parentId: 父级id</li>
 * <li>path: 层次路径</li>
 * <li>categoryDTOs: 子分类</li>
 * <li>resourceDTOs: 分类的项目</li>
 * </ul>
 */
public class ResourceCategoryDTO {
	
	private Long id;

	private String name;

	private Long parentId;

	private String path;

	@ItemType(ResourceCategoryAssignmentDTO.class)
	private List<ResourceCategoryAssignmentDTO>  resourceDTOs;

	@ItemType(ResourceCategoryDTO.class)
	private List<ResourceCategoryDTO> categoryDTOs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<ResourceCategoryAssignmentDTO> getResourceDTOs() {
		return resourceDTOs;
	}

	public void setResourceDTOs(List<ResourceCategoryAssignmentDTO> resourceDTOs) {
		this.resourceDTOs = resourceDTOs;
	}

	public List<ResourceCategoryDTO> getCategoryDTOs() {
		return categoryDTOs;
	}

	public void setCategoryDTOs(List<ResourceCategoryDTO> categoryDTOs) {
		this.categoryDTOs = categoryDTOs;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
