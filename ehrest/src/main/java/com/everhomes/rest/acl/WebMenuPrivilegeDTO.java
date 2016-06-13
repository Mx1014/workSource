package com.everhomes.rest.acl;

/**
 * <p>权限资源</p>
 * <ul>
 * <li>menuId: 菜单id</li>
 * <li>privilegeId: 权限id</li>
 * <li>name: 权限名称</li>
 * <li>discription: 权限描述</li>
 * <li>showFlag: 是否关联菜单</li>
 * <li>sortNum: 排序</li>
 * </ul>
 */
public class WebMenuPrivilegeDTO {
	
	private Long    menuId;
	private Long    privilegeId;
	private String  name;
	private Byte    showFlag;
	private String  discription;
	private Integer sortNum;
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	public Long getPrivilegeId() {
		return privilegeId;
	}
	public void setPrivilegeId(Long privilegeId) {
		this.privilegeId = privilegeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Byte getShowFlag() {
		return showFlag;
	}
	public void setShowFlag(Byte showFlag) {
		this.showFlag = showFlag;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	
	
	
}