package com.everhomes.rest.organization;


import com.everhomes.util.StringHelper;


/**
 * 
 * <ul>
 * 	<li>dto : 用户信息</li>
 * </ul>
 *
 */
public class VerifyPersonnelByPhoneCommandResponse {
	
	private OrganizationMemberDTO dto;



	public OrganizationMemberDTO getDto() {
		return dto;
	}




	public void setDto(OrganizationMemberDTO dto) {
		this.dto = dto;
	}




	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
