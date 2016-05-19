// @formatter:off
package com.everhomes.rest.namespace;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId：命名空间id</li>
 * </ul>
 */
public class GetNamespaceDetailCommand {
	private Integer namespaceId;
	
	public GetNamespaceDetailCommand() {
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
