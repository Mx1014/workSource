//@formatter:off
package com.everhomes.rest.address;

/**
 * Created by Wentian Wang on 2017/8/25.
 */

/**
 *<ul>
 * <li>buildingName:楼栋名</li>
 * <li>communityId:园区id</li>
 *</ul>
 */
public class GetApartmentNameByBuildingNameCommand {
    private String buildingName;
    private Long communityId;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
}
