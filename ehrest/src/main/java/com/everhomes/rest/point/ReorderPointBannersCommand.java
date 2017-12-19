package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>orders: 排序 {@link com.everhomes.rest.point.PointBannerOrder}</li>
 * </ul>
 */
public class ReorderPointBannersCommand {

    @ItemType(PointBannerOrder.class)
    private List<PointBannerOrder> orders;

    public List<PointBannerOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<PointBannerOrder> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
