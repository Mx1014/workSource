package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;

import java.io.Serializable;
import java.util.List;

/**
 * <p>web菜单</p>
 * <ul>
 * <li>id: 模块id</li>
 * <li>name: 模块名称</li>
 * <li>parentId: 父级id </li>
 * <li>path: 层次关系</li>
 * <li>level: 级别</li>
 * <li>serviceModules: 子业务模块</li>
 * </ul>
 */
public class ServiceModuleDTO implements Serializable {
	private Long id;

	private String name;

	private Long parentId;

	private String path;

	private Integer level;

	@ItemType(ServiceModuleDTO.class)
	private List<ServiceModuleDTO> serviceModules;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<ServiceModuleDTO> getServiceModules() {
		return serviceModules;
	}

	public void setServiceModules(List<ServiceModuleDTO> serviceModules) {
		this.serviceModules = serviceModules;
	}
}