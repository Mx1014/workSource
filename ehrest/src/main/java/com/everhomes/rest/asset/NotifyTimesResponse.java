package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>notifyTimes: 本月已催缴次数</li>
 *     <li>lastNotifyTime: 上次催缴时间</li>
 * </ul>
 */
public class NotifyTimesResponse {

    private Integer notifyTimes;

    private Timestamp lastNotifyTime;

    public Integer getNotifyTimes() {
        return notifyTimes;
    }

    public void setNotifyTimes(Integer notifyTimes) {
        this.notifyTimes = notifyTimes;
    }

    public Timestamp getLastNotifyTime() {
        return lastNotifyTime;
    }

    public void setLastNotifyTime(Timestamp lastNotifyTime) {
        this.lastNotifyTime = lastNotifyTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
