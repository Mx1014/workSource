package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>feedbackDtos: 举报列表 参考{@link com.everhomes.rest.user.FeedbackDTO}</li>
 *  <li>nextPageAnchor: 下页锚点</li>
 * </ul>
 */
public class ListFeedbacksResponse {
	@ItemType(FeedbackDTO.class)
	private List<FeedbackDTO> feedbackDtos;

	private Long nextPageAnchor;

	public List<FeedbackDTO> getFeedbackDtos() {
		return feedbackDtos;
	}

	public void setFeedbackDtos(List<FeedbackDTO> feedbackDtos) {
		this.feedbackDtos = feedbackDtos;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
