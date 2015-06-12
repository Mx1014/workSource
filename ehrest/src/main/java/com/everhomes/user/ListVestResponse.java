package com.everhomes.user;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListVestResponse {
    @ItemType(UserInfo.class)
    private List<UserInfo> values;

    private Long pageAnchor;

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public List<UserInfo> getValues() {
        return values;
    }

    public void setValues(List<UserInfo> values) {
        this.values = values;
    }
}
