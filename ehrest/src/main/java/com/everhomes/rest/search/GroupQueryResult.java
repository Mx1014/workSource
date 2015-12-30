package com.everhomes.rest.search;

import java.util.List;

import com.everhomes.discover.ItemType;

public class GroupQueryResult {
    private Long pageAnchor;
    
    @ItemType(Long.class)
    private List<Long> ids;
   
    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public List<Long> getIds() {
        return ids;
    }
    
    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
