package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>itemCategoryId: 分类id</li>
 * <li>defaultOrder: 排序</li>
 * </ul>
 */
public class PortalItemCategoryReorder {
    private Long itemCategoryId;
    private Integer defaultOrder;

    public Long getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }
}
