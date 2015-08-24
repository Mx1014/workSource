// @formatter:off
package com.everhomes.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>regionId: 区域id(城市或区县的id)</li>
 * <li>communityId: 小区id</li>
 * <li>address: 服务地址详情</li>
 * </ul>
 */
public class CreateServiceAddressCommand {
    
//    @NotNull
//    private Long cityId;
//    
//    @NotNull
//    private Long areaId;
    @NotNull
    private Long regionId;
    
    private Long communityId;
    
    @NotNull
    private String address;

    public CreateServiceAddressCommand() {
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
