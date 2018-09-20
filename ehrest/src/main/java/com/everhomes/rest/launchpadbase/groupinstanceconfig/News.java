package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.rest.launchpadbase.ItemGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>itemGroup: itemGroup</li>
 *     <li>categoryId: categoryId</li>
 *     <li>timeWidgetStyle: timeWidgetStyle</li>
 * </ul>
 */
public class News {

    private String itemGroup;
    private Integer categoryId;
    private String timeWidgetStyle;

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTimeWidgetStyle() {
        return timeWidgetStyle;
    }

    public void setTimeWidgetStyle(String timeWidgetStyle) {
        this.timeWidgetStyle = timeWidgetStyle;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
