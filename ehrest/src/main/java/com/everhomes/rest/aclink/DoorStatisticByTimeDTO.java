package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

public class DoorStatisticByTimeDTO {

    private Long logTime;

    private String createTime;

    private Long openNumber;

    public Long getOpenNumber() {
        return openNumber;
    }

    public void setOpenNumber(Long openNumber) {
        this.openNumber = openNumber;
    }

    public Long getLogTime() {
        return logTime;
    }

    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
