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
 * <li>dtos: 子菜单</li>
 * <li>conditionType：模块需要的条件类型目前定义四个值。  system：跟系统直接相关不需要条件（不需要有筛选条件）， namespace：前提必须要有域空间（有域空间的筛选条件），才能查看到内容， project：前提条件是项目，而查询项目的前提条件要有域空间（有域空间的筛选条件，联动出项目筛选条件）， organization：前提条件是机构，机构的前提条件要有域空间（有域空间的筛选条件，联动出机构的筛选条件）</li>
 * <li>category：类别，classify 菜单归类，module 模块， page 页面</li>
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

	private String conditionType;

	private String category;
	
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

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<WebMenuDTO> getDtos() {
		return dtos;
	}
	public void setDtos(List<WebMenuDTO> dtos) {
		this.dtos = dtos;
	}
	
}