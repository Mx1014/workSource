// @formatter:off
package com.everhomes.rest.scene;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID，不填则列全部</li>
 * </ul>
 */
public class ListSceneTypesCommand {
    private Integer namespaceId;
    
    public ListSceneTypesCommand() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
