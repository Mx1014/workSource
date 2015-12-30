// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>dateStr: 账单时间</li>
 * </ul>
 */
public class PropCommunityBillDateCommand {
    private Long communityId;
    
    private String dateStr;
   
    public PropCommunityBillDateCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	


	public String getDateStr() {
		return dateStr;
	}


	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
