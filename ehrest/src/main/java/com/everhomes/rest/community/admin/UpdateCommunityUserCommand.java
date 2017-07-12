package com.everhomes.rest.community.admin;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;

/**
 * <ul>
 * <li>userId: 用户id</li> 
 * <li>executiveFlag：是否高管 0-否 1-是</li>
 * <li>position：职位</li>
 * <li>identityNumber：身份证号</li>  
 * </ul>
 */
public class UpdateCommunityUserCommand {
 
	private Long     userId;
	 

    private Byte executiveFlag;
    private String position;
    private String identityNumber;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Byte getExecutiveFlag() {
		return executiveFlag;
	}
	public void setExecutiveFlag(Byte executiveFlag) {
		this.executiveFlag = executiveFlag;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getIdentityNumber() {
		return identityNumber;
	}
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}  
	
    
}
