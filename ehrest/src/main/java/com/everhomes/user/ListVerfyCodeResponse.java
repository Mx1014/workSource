package com.everhomes.user;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListVerfyCodeResponse {
    @ItemType(UserIdentifierDTO.class)
    private List<UserIdentifierDTO> values;

    private Long nextAnchor;

    public Long getNextAnchor() {
        return nextAnchor;
    }

    public void setNextAnchor(Long nextAnchor) {
        this.nextAnchor = nextAnchor;
    }

    public List<UserIdentifierDTO> getValues() {
        return values;
    }

    public void setValues(List<UserIdentifierDTO> values) {
        this.values = values;
    }

}
