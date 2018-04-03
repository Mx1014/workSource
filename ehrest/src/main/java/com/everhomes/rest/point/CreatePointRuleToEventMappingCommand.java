package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>eventName: eventName</li>
 *     <li>categoryId: categoryId</li>
 *     <li>ruleId: ruleId</li>
 * </ul>
 */
public class CreatePointRuleToEventMappingCommand {

    private String eventName;
    private Long categoryId;
    private Long ruleId;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
