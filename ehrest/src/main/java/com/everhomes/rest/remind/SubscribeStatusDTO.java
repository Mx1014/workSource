package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>remindId: 返回的ID有值，同时和当前日程ID相同表示已关注</li>
 * </ul>
 */
public class SubscribeStatusDTO {
    private Long remindId;

    public SubscribeStatusDTO() {

    }

    public SubscribeStatusDTO(Long remindId) {
        this.remindId = remindId;
    }

    public Long getRemindId() {
        return remindId;
    }

    public void setRemindId(Long remindId) {
        this.remindId = remindId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
