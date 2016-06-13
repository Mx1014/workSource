// @formatter:off
package com.everhomes.rest.category;

/**
 * <ul>
 * <li>parentId: 父亲ID</li>
 * <li>status: 状态，{@link com.everhomes.rest.category.CategoryAdminStatus}</li>
 * <li>sortOrder: 升降排序的方式，{@link com.everhomes.util.SortOrder}</li>
 * <li>namespaceId:名字空间ID</li>
 * </ul>
 */
public class ListCategoryCommand {
    private Long parentId;
    private Byte status;
    private Byte sortOrder;
    private Integer namespaceId;

    public ListCategoryCommand() {
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Byte sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    
}
