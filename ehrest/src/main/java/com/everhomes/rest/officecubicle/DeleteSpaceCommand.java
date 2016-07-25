package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 删除空间
 * <li>id: 空间id</li> 
 * </ul>
 */
public class DeleteSpaceCommand {

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
