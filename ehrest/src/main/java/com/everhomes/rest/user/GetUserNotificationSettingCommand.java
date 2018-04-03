// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>targetType: 实体类型 {@link com.everhomes.rest.common.EntityType}</li>
 *     <li>targetId: 实体id</li>
 * </ul>
 */
public class GetUserNotificationSettingCommand {

    @NotNull
    private String targetType;
    @NotNull
    private Long targetId;

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
