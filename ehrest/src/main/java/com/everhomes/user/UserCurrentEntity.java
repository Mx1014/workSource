// @formatter:off
package com.everhomes.user;

/**
 * 用户当前切换的实体，有可能是小区、家庭、机构等
 * <ul>
 * <li>entityType: 实体类型，参考{@link  com.everhomes.user.UserCurrentEntityType}</li>
 * <li>entityId: 实体对应的ID</li>
 * <li>timestamp: 切换的时间</li>
 * <li></li>
 * </ul>
 */
public class UserCurrentEntity {
    private String entityType;
    
    private String entityId;
    
    private Long timestamp;
    
    public UserCurrentEntity() {
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
}
