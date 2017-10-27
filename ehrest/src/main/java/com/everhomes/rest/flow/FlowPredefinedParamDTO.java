package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>entityType: entityType {@link com.everhomes.rest.flow.FlowEntityType}</li>
 *     <li>displayName: 显示名称</li>
 *     <li>text: 参数值</li>
 * </ul>
 */
public class FlowPredefinedParamDTO {

    private Long id;
    private String entityType;
    private String displayName;
    private String ownerType;
    private Long ownerId;
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
