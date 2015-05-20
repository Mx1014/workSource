// @formatter:off
package com.everhomes.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>cityId: 城市Id</li>
 * <li>keyword: 查询关键字</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class SearchCommunityCommand {
    private Long cityId;
    
    @NotNull
    private String keyword;
    
    // start from 1, page size is configurable at server side
    private Integer pageSize;
    
    public SearchCommunityCommand() {
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

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
