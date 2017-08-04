// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>entryTime: 入场时间</li>
 *     <li>exitTime: 出场时间</li>
 * </ul>
 */
public class ParkingActualClearanceLogDTO {

    private Long id;

    private Timestamp entryTime;
    private Timestamp exitTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Timestamp entryTime) {
        this.entryTime = entryTime;
    }

    public Timestamp getExitTime() {
        return exitTime;
    }

    public void setExitTime(Timestamp exitTime) {
        this.exitTime = exitTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
