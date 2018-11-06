package com.everhomes.rest.aclink;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间Id</li>
 * <li>namespaceName: 域空间名</li>
 * <li>ownerType(Byte)：门禁类型（0公共门禁，1企业门禁）</li>
 * <li>activeDoorNumber:已激活门禁数</li>
 * <li>activeCompanyDoorNumber：已激活企业门禁数</li>
 * <li>activePublicDoorNumber：已激活公共门禁数</li>
 * </ul>
 */
public class ActiveDoorByNamespaceDTO {
    private Long namespaceId;

    private String namespaceName;

    private Byte ownerType;

    private Integer activeDoorNumber;

    private Long activeCompanyDoorNumber;

    private Long activePublicDoorNumber;

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getActiveDoorNumber() {
        return activeDoorNumber;
    }

    public void setActiveDoorNumber(Integer activeDoorNumber) {
        this.activeDoorNumber = activeDoorNumber;
    }

    public Long getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Long namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public Long getActiveCompanyDoorNumber() {
        return activeCompanyDoorNumber;
    }

    public void setActiveCompanyDoorNumber(Long activeCompanyDoorNumber) {
        this.activeCompanyDoorNumber = activeCompanyDoorNumber;
    }

    public Long getActivePublicDoorNumber() {
        return activePublicDoorNumber;
    }

    public void setActivePublicDoorNumber(Long activePublicDoorNumber) {
        this.activePublicDoorNumber = activePublicDoorNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
