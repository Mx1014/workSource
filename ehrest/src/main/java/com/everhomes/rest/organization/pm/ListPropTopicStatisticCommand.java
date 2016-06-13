// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>categoryId: 帖子类型</li>
 * <li>startStrTime: 起始时间</li>
 * <li>endStrTime: 结束时间</li>
 * </ul>
 */
public class ListPropTopicStatisticCommand {
    private Long communityId;
    private Long categoryId;
	private String startStrTime;
	private String endStrTime;
 
    
    public ListPropTopicStatisticCommand() {
    }
    public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getStartStrTime() {
		return startStrTime;
	}
	public void setStartStrTime(String startStrTime) {
		this.startStrTime = startStrTime;
	}
	public String getEndStrTime() {
		return endStrTime;
	}
	public void setEndStrTime(String endStrTime) {
		this.endStrTime = endStrTime;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
