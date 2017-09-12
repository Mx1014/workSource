package com.everhomes.rest.portal;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *
 * <ul>
 * <li>itemCategoryId: 分类id</li>
 * <li>items: 分类下面的所有item</li>
 * </ul>
 */
public class PortalItemCategoryRank {
    private Long itemCategoryId;

    @ItemType(PortalItemReorder.class)
    private List<PortalItemReorder> items;

    public Long getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public List<PortalItemReorder> getItems() {
        return items;
    }

    public void setItems(List<PortalItemReorder> items) {
        this.items = items;
    }
}
