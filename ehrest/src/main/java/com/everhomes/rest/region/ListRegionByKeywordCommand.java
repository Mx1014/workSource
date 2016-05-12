// @formatter:off
package com.everhomes.rest.region;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parentId: 父亲区域的ID</li>
 * <li>scope: 范围，参考{@link com.everhomes.rest.region.RegionScope}</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.region.RegionAdminStatus}</li>
 * <li>sortBy</li>
 * <li>sortOrder: 升降排序的方式，{@link com.everhomes.util.SortOrder}</li>
 * <li>keyword: 关键字</li>
 * <li>namespaceId: 域空间</li>
 * </ul>
 */
public class ListRegionByKeywordCommand {
    private Long parentId;
    @NotNull
    private Byte scope;
    private Byte status;
    private String sortBy;
    private Byte sortOrder;
    @NotNull
    private String keyword;
    private Integer namespaceId;
    
    public ListRegionByKeywordCommand() {
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
