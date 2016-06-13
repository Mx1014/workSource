package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;
/**
 * 
 *<ul>
 *<li>namespaceId: 定制版本ID</li>
 *<li>communityId: 所在小区/园区ID</li>
 *<li>categoryId: 活动所在帖子的类型ID</li>
 *<li>tag:活动标签</li>
 * <li>pageAnchor: 开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 *</ul>
 */
public class ListActivitiesByNamespaceIdAndTagCommand {
    private Integer namespaceId;
    private Long communityId;
    private Long categoryId;
    private String tag;
    private Long pageAnchor;
    private Integer pageSize;

	public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
