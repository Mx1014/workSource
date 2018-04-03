package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 添加访客授权。
 * <li>phone: 电话</li>
 * <li>doorId: 门禁ID</li>
 * </ul>
 * @author janson
 *
 */
public class CreateDoorVisitorCommand {
    @NotNull
    private String phone;
    
    @NotNull
    private Long     doorId;
    
    @NotNull
    private Integer namespaceId;

    private String userName;
    private String visitorEvent;
    private String organization;
    private String description;
    private Long doorNumber;
    private String authMethod;
	private Long validFromMs;
	private Long validEndMs;
    
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Long getDoorId() {
        return doorId;
    }
    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }
    public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getVisitorEvent() {
        return visitorEvent;
    }
    public void setVisitorEvent(String visitorEvent) {
        this.visitorEvent = visitorEvent;
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
    public Long getDoorNumber() {
        return doorNumber;
    }
    public void setDoorNumber(Long doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getAuthMethod() {
        return authMethod;
    }
    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
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
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
