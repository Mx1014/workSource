package com.everhomes.rest.servicehotline;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * 
 * <li>id: id</li>   
 * </ul>
 */
public class DeleteHotlineCommand {
	private Long id;  

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	} 
}
