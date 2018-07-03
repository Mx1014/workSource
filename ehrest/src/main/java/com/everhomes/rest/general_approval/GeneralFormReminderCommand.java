package com.everhomes.rest.general_approval;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>sourceId: 源Id</li>
 * <li>sourceType: 源类型</li>
 * <li>currentOrganizationId: 企业id</li>
 * </ul>
 */
public class GeneralFormReminderCommand {

    private Integer namespaceId;

    private Long sourceId;

    private String sourceType;

    private Long ownerId;

    private String ownerType;

    private Long currentOrganizationId;

    public GeneralFormReminderCommand() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getCurrentOrganizationId() {
        return currentOrganizationId;
    }

    public void setCurrentOrganizationId(Long currentOrganizationId) {
        this.currentOrganizationId = currentOrganizationId;
    }
}
