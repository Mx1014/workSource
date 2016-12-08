package com.everhomes.rest.acl;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <p>web菜单</p>
 * <ul>
 * <li>id: 菜单id</li>
 * <li>name: 菜单名称</li>
 * <li>iconUrl: icon图</li>
 * <li>dataType: 菜单参数</li>
 * <li>leafFlag: 是否是页子节点，参考{@link com.everhomes.rest.acl.WebMenuLeafFlag}</li>
 * <li>sortNum: 排序号</li>
 * <li>parentId: 父级id</li>
 * </ul>
 */
public class WebMenuDTO {
	
	private Long    id;
	private String  name;
	private String  iconUrl;
	private String  dataType;
	private Byte    leafFlag;
	private Long    parentId;
	private Long moduleId;
	
	@ItemType(WebMenuDTO.class)
	private List<WebMenuDTO> dtos;
	
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
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Byte getLeafFlag() {
		return leafFlag;
	}
	public void setLeafFlag(Byte leafFlag) {
		this.leafFlag = leafFlag;
	}
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public List<WebMenuDTO> getDtos() {
		return dtos;
	}
	public void setDtos(List<WebMenuDTO> dtos) {
		this.dtos = dtos;
	}
	
}