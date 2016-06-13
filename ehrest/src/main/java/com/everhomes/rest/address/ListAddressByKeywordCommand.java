// @formatter:off
package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区Id</li>
 * <li>keyword: 查询关键字</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListAddressByKeywordCommand {
    @NotNull
    private Long communityId;
    
    @NotNull
    private String keyword;
    
    private Integer pageOffset;
    
    private Integer pageSize;

    public ListAddressByKeywordCommand() {
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
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
