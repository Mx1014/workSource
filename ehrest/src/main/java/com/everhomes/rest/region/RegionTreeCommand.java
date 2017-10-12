// @formatter:off
package com.everhomes.rest.region;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parentId: 父亲区域的ID, 不传则是最高层</li>
 * <li>namespaceId: 域空间id 没有默认取用户的</li>
 * </ul>
 */
public class RegionTreeCommand {
    private Long parentId;
    private Integer namespaceId;

    public RegionTreeCommand() {
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
}
