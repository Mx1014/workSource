// @formatter:off
package com.everhomes.rest.forum;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构ID</li>
 * <li>communityId: {@link com.everhomes.rest.forum.OrganizationTopicMixType}</li>
 * <li>excludeCategories: 不查询的内容类型 {@link com.everhomes.rest.category.CategoryConstants}</li>
 * <li>categoryId: 选择类型的Id，1-话题、1010-活动、1011-投票{@link com.everhomes.rest.category.CategoryConstants}</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>tag: 标签</li>
 * </ul>
 */
public class ListOrgMixTopicCommand {
    private Long organizationId;
    private String mixType;
    private Long pageAnchor;
    private Integer pageSize;
    
    @ItemType(Long.class)
    private List<Long> excludeCategories;

    private Long categoryId;

    private String tag;
    
    public ListOrgMixTopicCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getMixType() {
        return mixType;
    }

    public void setMixType(String mixType) {
        this.mixType = mixType;
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
