package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 *  <li>id  : id</li>
 *  <li>targetId  : 项目id</li>
 *  <li>count: count </li>
 */

public class OfflineTaskCountStat {
    private Long id ;
    private Long targetId ;
    private Long count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
