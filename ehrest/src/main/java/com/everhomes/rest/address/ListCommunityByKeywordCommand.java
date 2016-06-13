// @formatter:off
package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>cityId: 城市Id</li>
 * <li>keyword: 查询关键字</li>
 * <li>pageOffset: 页码</li>
 * </ul>
 */
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
