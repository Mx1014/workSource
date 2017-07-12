package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 *<ul>
 * <li>tag:活动标签</li>
 * <li>community_id:小区id</li>
 * <li>anchor:分页</li>
 * <li>officialFlag:是否是官方活动</li>
 * <li>range:范围,周边活动传入6；同城活动传入4</li>
 * <li>pageSize: 每页的数量</li>
 * <li>categoryId: 活动类型id</li>
 *</ul>
 */
public class ListActivitiesByTagCommand {
    private String tag;
    
    private Long community_id;
    
    private Long anchor;
    
    private int range;

    private Byte officialFlag;
    
    private Integer pageSize;
    
    private Long categoryId;

    private Long contentCategoryId;

    @ItemType(Integer.class)
    private List<Integer> activityStatusList;
    
    public Long getContentCategoryId() {
		return contentCategoryId;
	}

	public void setContentCategoryId(Long contentCategoryId) {
		this.contentCategoryId = contentCategoryId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getCommunity_id() {
		return community_id;
	}

	public void setCommunity_id(Long community_id) {
		this.community_id = community_id;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Byte getOfficialFlag() {
        return officialFlag;
    }

    public void setOfficialFlag(Byte officialFlag) {
        this.officialFlag = officialFlag;
    }

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<Integer> getActivityStatusList() {
        return activityStatusList;
    }

    public void setActivityStatusList(List<Integer> activityStatusList) {
        this.activityStatusList = activityStatusList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
