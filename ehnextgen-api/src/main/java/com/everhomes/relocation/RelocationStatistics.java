package com.everhomes.relocation;

import com.everhomes.util.StringHelper;

public class RelocationStatistics {

    private Byte status;
    private Integer count;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
