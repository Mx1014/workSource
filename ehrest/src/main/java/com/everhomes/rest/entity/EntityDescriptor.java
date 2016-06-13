// @formatter:off
package com.everhomes.rest.entity;

public class EntityDescriptor {
    private String entityType;
    private long entityId;
    
    public EntityDescriptor() {
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }
}
