package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 授权给的用户</li>
 * <li>doorId: 门禁 ID</li>
 * <li>validFromMs: 授权开始有效时间</li>
 * <li>validEndMs: 授权失效时间</li>
 * <li>totalAuthAmount:授权有效次数</li>
 * <li>organization: 用户来自于</li>
 * <li>description: 授权描述</li>
 * <li>notice: 访客来访提示 1 需提示 null 不提示（只在app端）</li>
 * </ul>
 * @author janson
 *
 */
public class CreateDoorAuthByUser {
    @NotNull
    private String phone;
    
    @NotNull
    private Long     doorId;
    
    @NotNull
    private Byte     authType;
    
    @NotNull
    private Integer namespaceId;
    
    @NotNull
    private Long     validFromMs;
    
    @NotNull
    private Long     validEndMs;

    private String organization;
    private String description;
    
    private String authMethod;
    private Integer totalAuthAmount;

    private Byte notice;

    public Byte getNotice() {
        return notice;
    }

    public void setNotice(Byte notice) {
        this.notice = notice;
    }

    public Integer getTotalAuthAmount() {
		return totalAuthAmount;
	}
	public void setTotalAuthAmount(Integer totalAuthAmount) {
		this.totalAuthAmount = totalAuthAmount;
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
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getAuthMethod() {
        return authMethod;
    }
    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
