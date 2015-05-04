// @formatter:off
package com.everhomes.category;

/**
 * <ul>
 * <li>parentId: 父亲ID</li>
 * <li>status: 状态，{@link com.everhomes.category.CategoryAdminStatus}</li>
 * <li>sortBy</li>
 * <li>sortOrder: 升降排序的方式，{@link com.everhomes.util.SortOrder}</li>
 * </ul>
 */
public class ListCategoryCommand {
    private Long parentId;
    private Byte status;
    private String sortBy;
    private Byte sortOrder;

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
}
