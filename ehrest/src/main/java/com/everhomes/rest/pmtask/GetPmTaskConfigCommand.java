package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>id: 主键</li>
 * <li>namespaceId: 域空间Id</li>
 * <li>ownerType: 项目类型</li>
 * <li>ownerId: 项目Id</li>
 * </ul>
 */
public class GetPmTaskConfigCommand {

    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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
}
