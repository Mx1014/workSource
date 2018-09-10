package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>openStartTime: 开门次数按时间统计起点</li>
 *     <li>openEndTime: 开门次数按时间统计终点</li>
 *     <li>tempAuthStartTime: 临时授权按时间统计终点</li>
 *     <li>tempAuthEndTime: 临时授权按时间统计终点</li>
 * </ul>
 */

public class QryDoorStatisticsCommand {

    private Long openStartTime;

    private Long openEndTime;

    private Long tempAuthStartTime;

    private Long tempAuthEndTime;

    private Long doorId;

    private Byte RightType;

    public Long getOpenStartTime() {
        return openStartTime;
    }

    public void setOpenStartTime(Long openStartTime) {
        this.openStartTime = openStartTime;
    }

    public Long getOpenEndTime() {
        return openEndTime;
    }

    public void setOpenEndTime(Long openEndTime) {
        this.openEndTime = openEndTime;
    }

    public Long getTempAuthStartTime() {
        return tempAuthStartTime;
    }

    public void setTempAuthStartTime(Long tempAuthStartTime) {
        this.tempAuthStartTime = tempAuthStartTime;
    }

    public Long getTempAuthEndTime() {
        return tempAuthEndTime;
    }

    public void setTempAuthEndTime(Long tempAuthEndTime) {
        this.tempAuthEndTime = tempAuthEndTime;
    }

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Byte getRightType() {
        return RightType;
    }

    public void setRightType(Byte rightType) {
        RightType = rightType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
