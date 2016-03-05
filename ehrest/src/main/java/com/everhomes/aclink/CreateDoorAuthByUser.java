package com.everhomes.aclink;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>userId: 授权给的用户</li>
 * <li>doorId: 门禁 ID</li>
 * <li>validFromMs: 授权开始有效时间</li>
 * <li>validEndMs: 授权失效时间</li>
 * <li>organization: 用户来自于</li>
 * <li>description: 授权描述</li>
 * </ul>
 * @author janson
 *
 */
public class CreateDoorAuthByUser {
    @NotNull
    private Long     doorId;
    
    @NotNull
    private Byte     authType;
    
    @NotNull
    private Long     validFromMs;
    
    @NotNull
    private Long     validEndMs;

    private String organization;
    private String description;
    
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
    
    
}
