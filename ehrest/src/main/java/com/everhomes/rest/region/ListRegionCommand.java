// @formatter:off
package com.everhomes.rest.region;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parentId: 父亲区域的ID</li>
 * <li>scope: 范围，参考{@link com.everhomes.rest.region.RegionScope}</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.region.RegionAdminStatus}</li>
 * <li>namespaceId: 域空间id 没有默认取用户的</li>
 * <li>sortBy</li>
 * <li>sortOrder: 升降排序的方式，{@link com.everhomes.util.SortOrder}</li>
 * <li>keyword: 关键字</li>
 * </ul>
 */
public class ListRegionCommand {
    private Long parentId;
    private Byte scope;
    private Byte status;
    private String sortBy;
    private Byte sortOrder;
    private Integer namespaceId;
    private String keyword;
    
    public ListRegionCommand() {
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getScope() {
        return scope;
    }

    public void setScope(Byte scope) {
        this.scope = scope;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Byte getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Byte sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
