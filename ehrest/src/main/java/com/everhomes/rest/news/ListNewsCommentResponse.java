// @formatter:off

package com.everhomes.rest.news;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 返回值
 * <li>nextPageAnchor: 分页的锚点，下一页开始取数据的位置</li>
 * <li>commentList: 评论列表，参考{@link com.everhomes.rest.news.NewsCommentDTO}</li>
 * </ul>
 */
public class ListNewsCommentResponse {
	private Long nextPageAnchor;
	@ItemType(NewsCommentDTO.class)
	private List<NewsCommentDTO> commentList;
	private Long commentCount;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<NewsCommentDTO> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<NewsCommentDTO> commentList) {
		this.commentList = commentList;
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