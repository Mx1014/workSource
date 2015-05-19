package com.everhomes.search;

import java.util.List;

public class GroupQueryResult {
    private Long pageAnchor;
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
