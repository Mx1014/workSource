package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>eventJson: 事件json字符串 {@link com.everhomes.bus.LocalEvent}</li>
 * </ul>
 */
public class PublishEventCommand {

    @NotNull
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
