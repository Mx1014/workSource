package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>itemId: itemGroup id</li>
 * <li>defaultOrder: 排序</li>
 * </ul>
 */
public class PortalItemGroupReorder {
    private Long itemGroupId;
    private Integer defaultOrder;

    public Long getItemGroupId() {
        return itemGroupId;
    }

    public void setItemGroupId(Long itemGroupId) {
        this.itemGroupId = itemGroupId;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }
}
