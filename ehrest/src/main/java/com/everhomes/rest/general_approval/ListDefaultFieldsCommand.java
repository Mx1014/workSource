package com.everhomes.rest.general_approval;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>organizationId: 公司id</li>
 *     <li>moduleId: 模块id</li>
 *     <li>ownerType: ownerType</li>
 *     <li>ownerId: ownerId</li>
 * </ul>
 */
public class ListDefaultFieldsCommand {

    private Integer namespaceId; // 域空间id
    private Long organizationId; // 公司id
    private Long moduleId; // 模块id
    private String ownerType; //ownerType
    private Long ownerId;// ownerId

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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


    @Override
    public String toString() {
        return "ListDefaultFieldsCommand{" +
                "namespaceId=" + namespaceId +
                ", organizationId=" + organizationId +
                ", moduleId=" + moduleId +
                ", ownerType='" + ownerType + '\'' +
                ", ownerId=" + ownerId +
                '}';
    }
}
