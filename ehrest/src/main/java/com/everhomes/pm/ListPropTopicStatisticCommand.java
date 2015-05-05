// @formatter:off
package com.everhomes.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>categoryId: 帖子类型</li>
 * <li>startStrTime: 起始时间</li>
 * <li>endStrTime: 结束时间</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListPropTopicStatisticCommand {
    private Long communityId;
    private Byte categoryId;
	private String startStrTime;
	private String endStrTime;
    
	private Long pageOffset;
    private Long pageSize;
    
    public ListPropTopicStatisticCommand() {
    }
    public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public Byte getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Byte categoryId) {
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
	public Long getPageOffset() {
		return pageOffset;
	}
	public void setPageOffset(Long pageOffset) {
		this.pageOffset = pageOffset;
	}
	public Long getPageSize() {
		return pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
