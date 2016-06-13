// @formatter:off
package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>replacedAddressId: 地址Id，空则表示新增地址，否则修改地址</li>
 * <li>communityId: 小区Id</li>
 * <li>buildingName: 楼栋号</li>
 * <li>apartmentName: 门牌号</li>
 * <li>historyId: 历史表ID</li>
 * </ul>
 */
public class ClaimAddressCommand {
    // null means to claim a new family address, otherwise, it is to replace with the existing one
    private Long replacedAddressId;
    
    @NotNull
    private Long communityId;
    
    @NotNull
    private String buildingName;
    
    @NotNull
    private String apartmentName;
    
    @NotNull
    private Long historyId;

    public ClaimAddressCommand() {
    }

    public Long getReplacedAddressId() {
        return replacedAddressId;
    }

    public void setReplacedAddressId(Long addressId) {
        this.replacedAddressId = addressId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }
    
    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
