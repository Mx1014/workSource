// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>doorGuardList: (必填)门禁列表，{@link com.everhomes.rest.visitorsys.BaseDoorGuardDTO}</li>
 * </ul>
 */
public class ListDoorGuardsResponse {
    @ItemType(BaseDoorGuardDTO.class)
    private List<BaseDoorGuardDTO> doorGuardList;

    public List<BaseDoorGuardDTO> getDoorGuardList() {
        return doorGuardList;
    }

    public void setDoorGuardList(List<BaseDoorGuardDTO> doorGuardList) {
        this.doorGuardList = doorGuardList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}