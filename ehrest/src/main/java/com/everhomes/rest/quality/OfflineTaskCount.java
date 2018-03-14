package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 *  <li>id  : id</li>
 *  <li>targetId  : 项目id</li>
 *  <li>count: count </li>
 *  <li>type: 类型  0：已执行   1： 总数量 </li>
 */

public class OfflineTaskCount {
    private Long id ;
    private Long targetId ;
    private Long count;
    private Byte type;

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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

