package com.everhomes.rest.organization;


/**
 * <ul>
 * <li>id：organizationid</li>
 * <li>parentId：父机构id。没有填科技园的organizationid</li>
 * <li>name：名称</li>
 * </ul>
 */
public class UpdateOrganizationsCommand {
	
	private Long id;

	private String name;
	
	private Long parentId;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


}
