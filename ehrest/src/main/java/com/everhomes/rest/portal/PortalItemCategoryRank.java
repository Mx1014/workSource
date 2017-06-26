package com.everhomes.rest.portal;

import java.util.List;

/**
 *
 * <ul>
 * <li>itemCategoryId: 分类id</li>
 * <li>itemIds: 分类下面的所有item</li>
 * </ul>
 */
public class PortalItemCategoryRank {
    private Long itemCategoryId;
    private List<Long> itemIds;

    public Long getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}
