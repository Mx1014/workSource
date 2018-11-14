package com.everhomes.rest.customer;

public class createSuperAdminCommand {

    private String entries;
    private Long organizationId;
    private Integer namespaceId;
    private Byte isSigned;
    private Byte isJoined;
    private String contactor;

    private Long customerId;

    private Long communityId;
    private Long orgId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(Byte isSigned) {
        this.isSigned = isSigned;
    }

    public Byte getIsJoined() {
        return isJoined;
    }

    public void setIsJoined(Byte isJoined) {
        this.isJoined = isJoined;
    }

    public String getContactor() {
        return contactor;
    }

    public void setContactor(String contactor) {
        this.contactor = contactor;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
