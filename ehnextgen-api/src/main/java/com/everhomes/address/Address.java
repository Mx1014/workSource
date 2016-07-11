// @formatter:off
package com.everhomes.address;

import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.util.StringHelper;

public class Address extends EhAddresses {
	
	private Byte memberStatus;
	
    private static final long serialVersionUID = 6886544399208678098L;

    public Address() {
    }
    
    
    public Byte getMemberStatus() {
		return memberStatus;
	}


	public void setMemberStatus(Byte memberStatus) {
		this.memberStatus = memberStatus;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
