package com.everhomes.rest.rentalv2.admin;

/**
 * <ul>
 * delete 资源 
 * <li>id: 资源id</li>  
 * </ul>
 */
public class DeleteResourceCommand {

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
