// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>building : 楼栋</li>
  *<li>doorplate : 门牌</li>
  *</ul>
  */

public class VisitorSysBuilding {
    private String building;
    private String doorplate;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getDoorplate() {
        return doorplate;
    }

    public void setDoorplate(String doorplate) {
        this.doorplate = doorplate;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
