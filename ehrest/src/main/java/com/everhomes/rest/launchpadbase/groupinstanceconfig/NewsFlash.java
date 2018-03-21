package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.rest.launchpadbase.ItemGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>itemGroup: itemGroup</li>
 *     <li>categoryId: categoryId</li>
 *     <li>newsSize: newsSize</li>
 * </ul>
 */
public class NewsFlash {

    private String itemGroup;
    private Integer categoryId;
    private Integer newsSize;

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

    public Integer getNewsSize() {
        return newsSize;
    }

    public void setNewsSize(Integer newsSize) {
        this.newsSize = newsSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
