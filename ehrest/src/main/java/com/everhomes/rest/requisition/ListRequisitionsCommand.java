//@formatter:off
package com.everhomes.rest.requisition;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>theme: 请示主题</li>
 * <li>typeId: 请示类型id</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 页大小</li>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者id</li>
 * <li>namespaceId:域名id</li>
 * </ul>
 */
public class ListRequisitionsCommand {
    private String theme;
    private Long typeId;
    private Long pageAnchor;
    private Integer pageSize;
    private String ownerType;
    private Long ownerId;
    private Integer namespaceId;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
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
