// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ownerType: 实体类型 {@link com.everhomes.rest.common.EntityType}</li>
 *     <li>ownerId: 实体id</li>
 *     <li>targetType: 实体类型 {@link com.everhomes.rest.common.EntityType}</li>
 *     <li>targetId: 实体id</li>
 *     <li>muteFlag: 免打扰状态{@link com.everhomes.rest.user.UserMuteNotificationFlag}</li>
 * </ul>
 */
public class UserNotificationSettingDTO {

    private String ownerType;
    private Long ownerId;
    private String targetType;
    private Long targetId;

    private Byte muteFlag;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

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

    public Byte getMuteFlag() {
        return muteFlag;
    }

    public void setMuteFlag(Byte muteFlag) {
        this.muteFlag = muteFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
