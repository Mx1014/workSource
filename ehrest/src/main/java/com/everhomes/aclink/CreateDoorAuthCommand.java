package com.everhomes.aclink;

import javax.validation.constraints.NotNull;

public class CreateDoorAuthCommand {
    @NotNull
    private Long     userId;
    
    @NotNull
    private Long     doorId;
    
    @NotNull
    private Long approveUserId;
    
    @NotNull
    private Byte     authType;
    
    private Long     validFromMs;
    private Long     validEndMs;
    private String organization;
    private String description;
    
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getDoorId() {
        return doorId;
    }
    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }
    public Byte getAuthType() {
        return authType;
    }
    public void setAuthType(Byte authType) {
        this.authType = authType;
    }
    public Long getValidFromMs() {
        return validFromMs;
    }
    public void setValidFromMs(Long validFromMs) {
        this.validFromMs = validFromMs;
    }
    public Long getValidEndMs() {
        return validEndMs;
    }
    public void setValidEndMs(Long validEndMs) {
        this.validEndMs = validEndMs;
    }
    public String getOrganization() {
        return organization;
    }
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getApproveUserId() {
        return approveUserId;
    }
    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }
    
}
