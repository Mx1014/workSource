package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>activeDoor:已激活门禁数</li>
 *     <li>openTotal：开门次数合计</li>
 *     <li>tempAuthTotal：临时授权合计</li>
 * </ul>
 */

public class DoorStatisticDTO {

    private Long activeDoor;

    private Long openTotal;

    private Long tempAuthTotal;

    private Long permAuthTotal;

    public Long getActiveDoor() {
        return activeDoor;
    }

    public void setActiveDoor(Long activeDoor) {
        this.activeDoor = activeDoor;
    }

    public Long getOpenTotal() {
        return openTotal;
    }

    public void setOpenTotal(Long openTotal) {
        this.openTotal = openTotal;
    }

    public Long getTempAuthTotal() {
        return tempAuthTotal;
    }

    public void setTempAuthTotal(Long tempAuthTotal) {
        this.tempAuthTotal = tempAuthTotal;
    }

    public Long getPermAuthTotal() {
        return permAuthTotal;
    }

    public void setPermAuthTotal(Long permAuthTotal) {
        this.permAuthTotal = permAuthTotal;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
