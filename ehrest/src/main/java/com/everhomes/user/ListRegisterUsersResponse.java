package com.everhomes.user;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListRegisterUsersResponse {
    @ItemType(UserInfo.class)
    private List<UserInfo> values;

    private Long nextAnchor;

    public Long getNextAnchor() {
        return nextAnchor;
    }

    public void setNextAnchor(Long nextAnchor) {
        this.nextAnchor = nextAnchor;
    }

    public List<UserInfo> getValues() {
        return values;
    }

    public void setValues(List<UserInfo> values) {
        this.values = values;
    }
}
