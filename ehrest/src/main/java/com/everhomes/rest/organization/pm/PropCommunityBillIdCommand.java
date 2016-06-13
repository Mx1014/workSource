// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>billId: 账单id</li>
 * </ul>
 */
public class PropCommunityBillIdCommand {
    private Long communityId;
    
    private int billId;
   
    public PropCommunityBillIdCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	
	public int getBillId() {
		return billId;
	}


	public void setBillId(int billId) {
		this.billId = billId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
