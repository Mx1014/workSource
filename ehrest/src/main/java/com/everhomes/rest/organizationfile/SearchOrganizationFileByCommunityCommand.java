// @formatter:off
package com.everhomes.rest.organizationfile;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 当前用户所在的公司id</li>
 *     <li>communityId: 小区id</li>
 *     <li>keyword: 关键字</li>
 *     <li>pageAnchor: 下一页锚点</li>
 *     <li>pageSize: 每页数量</li>
 * </ul>
 */
public class SearchOrganizationFileByCommunityCommand {

    private Long organizationId;
    private Long communityId;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
