// @formatter:off
package com.everhomes.rest.announcement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 项目ID</li>
 * <li>publishStatus: 帖子发布状态，{@link com.everhomes.rest.announcement.AnnouncementPublishStatus}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListAnnouncementCommand {

    private Long communityId;

    private String publishStatus;

    private Long pageAnchor;

    private Integer pageSize;

    public ListAnnouncementCommand() {
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
