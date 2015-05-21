// @formatter:off
package com.everhomes.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区Id</li>
 * <li>keyword: 查询关键字</li>
 * </ul>
 */
public class ListAddressByKeywordCommand {
    @NotNull
    private Long communityId;
    
    @NotNull
    private String keyword;

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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
