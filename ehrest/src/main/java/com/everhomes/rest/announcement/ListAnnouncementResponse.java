package com.everhomes.rest.announcement;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>announcementDTOs: 帖或评论信息，参考{@link AnnouncementDTO}</li>
 * </ul>
 */
public class ListAnnouncementResponse {
    private Long nextPageAnchor;

    @ItemType(PostDTO.class)
    private List<AnnouncementDTO> announcementDTOs;

    private Long commentCount;

    private String keywords;

    public ListAnnouncementResponse() {
    }

    public ListAnnouncementResponse(Long nextPageAnchor, List<AnnouncementDTO> announcementDTOs) {
        this.nextPageAnchor = nextPageAnchor;
        this.announcementDTOs = announcementDTOs;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<AnnouncementDTO> getAnnouncementDTOs() {
        return announcementDTOs;
    }

    public void setAnnouncementDTOs(List<AnnouncementDTO> announcementDTOs) {
        this.announcementDTOs = announcementDTOs;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
