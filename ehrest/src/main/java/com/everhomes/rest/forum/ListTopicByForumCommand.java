// @formatter:off
package com.everhomes.rest.forum;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumIdList: 论坛ID列表</li>
 * <li>communityId: 当前小区ID，仅社区圈有效</li>
 * <li>communityId: 当前小区ID，仅社区圈有效</li>
 * <li>excludeCategories: 不查询的内容类型 {@link com.everhomes.rest.category.CategoryConstants}</li>
 * <li>categoryId: 选择类型的Id，1-话题、1010-活动、1011-投票{@link com.everhomes.rest.category.CategoryConstants}</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>tag: 标签</li>
 * </ul>
 */
public class ListTopicByForumCommand {
    @ItemType(Long.class)
    private List<Long> forumIdList;
    
    private Long communityId;
    
    private Long pageAnchor;
    
    private Integer pageSize;
    
    @ItemType(Long.class)
    private List<Long> excludeCategories;

    private Long categoryId;

    private String tag;

    public ListTopicByForumCommand() {
    }
	
    public List<Long> getForumIdList() {
        return forumIdList;
    }

    public void setForumIdList(List<Long> forumIdList) {
        this.forumIdList = forumIdList;
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

    
    
    public List<Long> getExcludeCategories() {
		return excludeCategories;
	}

	public void setExcludeCategories(List<Long> excludeCategories) {
		this.excludeCategories = excludeCategories;
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
