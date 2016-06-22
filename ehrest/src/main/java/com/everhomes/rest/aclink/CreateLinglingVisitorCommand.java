package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 令令添加访客授权。
 * <li>phone: 电话</li>
 * <li></li>
 * </ul>
 * @author janson
 *
 */
public class CreateLinglingVisitorCommand {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
