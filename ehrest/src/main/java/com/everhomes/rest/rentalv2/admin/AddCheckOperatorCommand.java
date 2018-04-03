package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>organizationId：授权的organization id</li> 
 * <li>userId：增加签到人的用户id</li> 
 * </ul>
 */
public class AddCheckOperatorCommand {
	private Long organizationId;
	private Long userId;

	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
 
 
 
}
