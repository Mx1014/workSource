// @formatter:off
package com.everhomes.address;

import javax.validation.constraints.NotNull;

public class ListCommunityByKeywordCommand {
    private Long cityId;
    
    @NotNull
    private String keyword;
    
    // start from 1, page size is configurable at server side
    private Long pageOffset;
    
    public ListCommunityByKeywordCommand() {
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }
}
