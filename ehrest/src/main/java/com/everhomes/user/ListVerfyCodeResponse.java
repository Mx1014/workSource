package com.everhomes.user;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListVerfyCodeResponse {
    @ItemType(UserIdentifierDTO.class)
    private List<UserIdentifierDTO> values;

    private Long pageAnchor;

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public List<UserIdentifierDTO> getValues() {
        return values;
    }

    public void setValues(List<UserIdentifierDTO> values) {
        this.values = values;
    }

}
