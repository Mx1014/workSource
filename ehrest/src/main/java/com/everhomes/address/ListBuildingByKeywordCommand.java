// @formatter:off
package com.everhomes.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communitId: 小区Id</li>
 * <li>keyword: 查询关键字</li>
 * </ul>
 */
public class ListBuildingByKeywordCommand {
    @NotNull
    private Long communitId;
    
    @NotNull
    private String keyword;
    
    public ListBuildingByKeywordCommand() {
    }

    public Long getCommunitId() {
        return communitId;
    }

    public void setCommunitId(Long communitId) {
        this.communitId = communitId;
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
