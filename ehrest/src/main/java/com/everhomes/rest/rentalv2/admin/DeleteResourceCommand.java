package com.everhomes.rest.rentalv2.admin;

/**
 * <ul>
 * delete 资源 
 * <li>id: 资源id</li>  
 * </ul>
 */
public class DeleteResourceCommand {

	private String resourceType;
	private Long id;

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
