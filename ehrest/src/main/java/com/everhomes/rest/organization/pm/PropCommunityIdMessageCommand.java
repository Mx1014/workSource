// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 物业小区id</li>
 * <li>message: 消息内容</li>
 * </ul>
 */
public class PropCommunityIdMessageCommand {
    private Long communityId;
   
    private String message;
    
    public PropCommunityIdMessageCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	

	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
