package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 客户id</li>
 *     <li>trackingUid：跟进人</li>
 * </ul>
 */
public class AllotEnterpriseCustomerCommand {

    private Long id;

    
    private  Long trackingUid;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    

    public Long getTrackingUid() {
		return trackingUid;
	}

	public void setTrackingUid(Long trackingUid) {
		this.trackingUid = trackingUid;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
