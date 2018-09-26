package com.everhomes.rest.aclink;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间Id</li>
 * <li>namespaceName: 域空间名</li>
 * <li>useNumber：功能使用次数数</li>
 * </ul>
 */
public class AclinkUseByNamespaceDTO {
    private Long namespaceId;

    private String namespaceName;

    private Long useNumber;

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

    public Long getUseNumber() {
        return useNumber;
    }

    public void setUseNumber(Long useNumber) {
        this.useNumber = useNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
