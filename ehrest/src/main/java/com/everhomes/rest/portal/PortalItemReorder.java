package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>itemId: item id</li>
 * <li>defaultOrder: 排序</li>
 * <li>moreOrder：分类里面的排序</li>
 * <li>displayFlag: 是否推荐致首页</li>
 * </ul>
 */
public class PortalItemReorder {
    private Long itemId;
    private Integer defaultOrder;
    private Byte displayFlag;
    private Integer moreOrder;

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

    public Byte getDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(Byte displayFlag) {
        this.displayFlag = displayFlag;
    }

    public Integer getMoreOrder() {
        return moreOrder;
    }

    public void setMoreOrder(Integer moreOrder) {
        this.moreOrder = moreOrder;
    }
}
