package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>eventJson: 事件json字符串 {@link com.everhomes.bus.LocalEvent}</li>
 * </ul>
 */
public class PublishEventCommand {

    private String eventJson;

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
