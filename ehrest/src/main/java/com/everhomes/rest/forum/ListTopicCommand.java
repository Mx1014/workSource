// @formatter:off
package com.everhomes.rest.forum;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>visibilityScope: 可见性范围类型，仅社区圈有效，{@link com.everhomes.rest.visibility.VisibilityScope}</li>
 * <li>communityId: 当前小区ID，仅社区圈有效</li>
 * <li>excludeCategories: 排除类型</li>
 * <li>categoryId: 选择类型的Id，1-话题、1010-活动、1011-投票 参考{@link com.everhomes.rest.category.CategoryConstants}</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>needTemporary: 0-不需要， 1-需要， 是否需要查询暂存的活动，用于后台查询时把暂存的贴子也查出来。 不填默认0</li>
 * </ul>
 */
public class ListTopicCommand {
    private Long forumId;
    private Byte visibilityScope;
    private Long communityId;
    private Long pageAnchor;
    private Integer pageSize;
    
    @ItemType(Long.class)
    private List<Long> excludeCategories;

    private Long categoryId;
    
    private Byte needTemporary;
    
    public ListTopicCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }
    
    public Byte getVisibilityScope() {
		return visibilityScope;
	}

	public void setVisibilityScope(Byte visibilityScope) {
		this.visibilityScope = visibilityScope;
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

    public Byte getNeedTemporary() {
		return needTemporary;
	}

	public void setNeedTemporary(Byte needTemporary) {
		this.needTemporary = needTemporary;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
