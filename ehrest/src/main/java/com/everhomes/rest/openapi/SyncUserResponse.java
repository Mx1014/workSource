package com.everhomes.rest.openapi;

public class SyncUserResponse {
    private Long userId;
    private java.sql.Timestamp timestamp;
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
