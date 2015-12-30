// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>buildingName: 楼栋名</li>
 * <li>buildingAliasName: 楼栋别名</li>
 * </ul>
 */
public class BuildingDTO {
    private java.lang.String   buildingName;
    private java.lang.String   buildingAliasName;
    
    public BuildingDTO() {
    }

    public java.lang.String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(java.lang.String buildingName) {
        this.buildingName = buildingName;
    }

    public java.lang.String getBuildingAliasName() {
        return buildingAliasName;
    }

    public void setBuildingAliasName(java.lang.String buildingAliasName) {
        this.buildingAliasName = buildingAliasName;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
