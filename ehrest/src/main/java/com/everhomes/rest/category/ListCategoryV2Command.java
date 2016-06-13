// @formatter:off
package com.everhomes.rest.category;

/**
 * <ul>
 * <li>namespaceId:名字空间ID</li>
 * </ul>
 */
public class ListCategoryV2Command {
    private Integer namespaceId;

    public ListCategoryV2Command() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    
}
