package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListVerfyCodeResponse {
    @ItemType(UserIdentifierDTO.class)
    private List<UserIdentifierDTO> values;

    private Long nextPageAnchor;

    public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<UserIdentifierDTO> getValues() {
        return values;
    }

    public void setValues(List<UserIdentifierDTO> values) {
        this.values = values;
    }

}
