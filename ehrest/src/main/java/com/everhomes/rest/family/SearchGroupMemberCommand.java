package com.everhomes.rest.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>keyword: 搜索关键字 用户姓名</li>
 *     <li>communityId: 小区id</li>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: 页面大小</li>
 * </ul>
 */
public class SearchGroupMemberCommand {

    private String keyword;

    private Long communityId;

    private Long pageAnchor;

    private Integer pageSize;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
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
