package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListVestResponse {
    @ItemType(UserInfo.class)
    private List<UserInfo> values;

    private Long nextPageAnchor;

    public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<UserInfo> getValues() {
        return values;
    }

    public void setValues(List<UserInfo> values) {
        this.values = values;
    }
}
