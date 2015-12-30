// @formatter:off
package com.everhomes.rest.namespace;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId：命名空间id</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListCommunityByNamespaceCommand {
	private Integer namespaceId;
    
    private Long pageAnchor;
    
    private Integer pageSize;
	
	public ListCommunityByNamespaceCommand() {
    }

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
