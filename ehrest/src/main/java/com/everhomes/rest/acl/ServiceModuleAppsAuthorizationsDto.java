package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

public class ServiceModuleAppsAuthorizationsDto {

    private String ownerType;
    private Long ownerId;
    private Long targetId;
    private String targetType;
    private String contactName;
    private String nickName;
    private String identifierToken;
    private Byte allFlag;
    private ServiceModuleAuthorizationsDTO communityControlApps;
    private ServiceModuleAuthorizationsDTO orgControlApps;
    private ServiceModuleAuthorizationsDTO unlimitControlApps;

    public ServiceModuleAuthorizationsDTO getCommunityControlApps() {
        return communityControlApps;
    }

    public void setCommunityControlApps(ServiceModuleAuthorizationsDTO communityControlApps) {
        this.communityControlApps = communityControlApps;
    }

    public ServiceModuleAuthorizationsDTO getOrgControlApps() {
        return orgControlApps;
    }

    public void setOrgControlApps(ServiceModuleAuthorizationsDTO orgControlApps) {
        this.orgControlApps = orgControlApps;
    }

    public ServiceModuleAuthorizationsDTO getUnlimitControlApps() {
        return unlimitControlApps;
    }

    public void setUnlimitControlApps(ServiceModuleAuthorizationsDTO unlimitControlApps) {
        this.unlimitControlApps = unlimitControlApps;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

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

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    public Byte getAllFlag() {
        return allFlag;
    }

    public void setAllFlag(Byte allFlag) {
        this.allFlag = allFlag;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
