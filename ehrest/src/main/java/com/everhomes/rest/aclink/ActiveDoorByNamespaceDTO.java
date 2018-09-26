package com.everhomes.rest.aclink;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间Id</li>
 * <li>namespaceName: 域空间名</li>
 * <li>activeCompanyDoorNumber：已激活企业门禁数</li>
 * <li>activePublicDoorNumber：已激活公共门禁数</li>
 * </ul>
 */
public class ActiveDoorByNamespaceDTO {
    private Long namespaceId;

    private String namespaceName;

    private Long activeCompanyDoorNumber;

    private Long activePublicDoorNumber;

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
