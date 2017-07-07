package com.everhomes.rest.acl.admin;

/**
 * <ul>
 * <li>id: 角色id</li>
 * <li>appId: 应用id</li>
 * <li>name: 权限名称 </li>
 * <li>description: 描述 </li>
 * </ul>
 */
public class RoleDTO {

	private Long id;
	
	private Long appId;
	
	private String name;
	
	private String description;

	private Long creatorUid;

	private Long createTime;

	private String creatorUName;

	public RoleDTO(){

	}

	public RoleDTO(Long id, String name){
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getCreatorUName() {
		return creatorUName;
	}

	public void setCreatorUName(String creatorUName) {
		this.creatorUName = creatorUName;
	}
}
