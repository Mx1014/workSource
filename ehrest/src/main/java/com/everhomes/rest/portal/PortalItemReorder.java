package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>itemId: item id</li>
 * <li>defaultOrder: 排序</li>
 * </ul>
 */
public class PortalItemReorder {
    private Long itemId;
    private Integer defaultOrder;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }
}
