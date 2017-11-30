package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>syncFlag: 同步还是异步, 同步表示事件处理成功才会返回，异步表示事件接收成功就返回 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>eventJson: 事件json</li>
 * </ul>
 */
public class PublishEventCommand {

    private Byte syncFlag;
    private String eventJson;

    public Byte getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(Byte syncFlag) {
        this.syncFlag = syncFlag;
    }

    public String getEventJson() {
        return eventJson;
    }

    public void setEventJson(String eventJson) {
        this.eventJson = eventJson;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
