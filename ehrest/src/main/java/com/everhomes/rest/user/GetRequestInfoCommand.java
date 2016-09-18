package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id:申请信息主键id</li>
 * </ul>
 */
public class GetRequestInfoCommand {
	
	private Long id;

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
