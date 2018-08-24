// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parentName: 父名称，如果不传此值，则获取省，如果传为省名称，则获取省下所有城市</li>
 * <li>namespaceId: 域空间id</li>
 * <li>nextPageAnchor: 下一页锚点</li>
 * <li>pageSize: 页大小</li>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目（为空代表通用配置）</li>
 * </ul>
 */
public class ListCitiesCommand {
	
	private String parentName;

    private Integer namespaceId;

    private Long nextPageAnchor;

    private Integer pageSize;

//  标准版兼容通用
    private String ownerType;
    private Long ownerId;

    public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
