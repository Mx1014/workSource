package com.everhomes.rest.techpark.rental.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * 
 * <li>id: </li> 
 * </ul>
 */
public class OpenResourceTypeCommand {

	private java.lang.Long    id; 
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	} 
}
