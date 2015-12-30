// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 当前小区ID</li>
 * <li>categoryId: 帖子类型ID</li>
 * <li>pageAnchor: 开始的锚点</li>
 * <li>tag: 帖子标签</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListActivityTopicByCategoryAndTagCommand {
    private Long communityId;
    private Long categoryId;
    private String tag;
    private Long pageAnchor;
    private Integer pageSize;
    
    public ListActivityTopicByCategoryAndTagCommand() {
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
