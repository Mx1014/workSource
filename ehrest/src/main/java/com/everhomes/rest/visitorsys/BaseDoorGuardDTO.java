// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
  *<ul>
 *<li>doorGuardId : (必填)门禁id</li>
 *<li>doorGuardName : (必填)门禁名称</li>
  *</ul>
  */
public class BaseDoorGuardDTO {
    private String doorGuardId;
    private String doorGuardName;

    public String getDoorGuardId() {
        return doorGuardId;
    }

    public void setDoorGuardId(String doorGuardId) {
        this.doorGuardId = doorGuardId;
    }

    public String getDoorGuardName() {
        return doorGuardName;
    }

    public void setDoorGuardName(String doorGuardName) {
        this.doorGuardName = doorGuardName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
