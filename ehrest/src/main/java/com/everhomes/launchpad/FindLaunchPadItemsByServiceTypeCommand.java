// @formatter:off
package com.everhomes.launchpad;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>serviceType: </li>
 * <li>communityId: 小区id</li>
 * </ul>
 */
public class FindLaunchPadItemsByServiceTypeCommand {
    
    @NotNull
    private Byte    serviceType;
    @NotNull
    private Long    communityId;

    public FindLaunchPadItemsByServiceTypeCommand() {
    }

    public Byte getServiceType() {
        return serviceType;
    }

    public void setServiceType(Byte serviceType) {
        this.serviceType = serviceType;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
