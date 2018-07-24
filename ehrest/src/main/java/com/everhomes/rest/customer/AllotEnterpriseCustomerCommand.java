package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 客户id</li>
 *     <li>trackingUid：跟进人</li>
 *     <li>deviceType：客户端类型</li>
 * </ul>
 */
public class AllotEnterpriseCustomerCommand {
    private Long id;
    private  Long trackingUid;
    private Long orgId;
    private  Byte deviceType ;

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

    public Byte getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Byte deviceType) {
        this.deviceType = deviceType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
