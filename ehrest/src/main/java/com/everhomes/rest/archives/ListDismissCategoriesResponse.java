package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>reasons: 离职原因</li>
 * </ul>
 */
public class ListDismissCategoriesResponse {

    @ItemType(String.class)
    private List<String> reasons;

    public ListDismissCategoriesResponse() {
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
