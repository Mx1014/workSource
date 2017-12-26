// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
import java.util.List;


/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>commentDtos: 评论列表，参考 {@link com.everhomes.rest.comment.CommentDTO}</li>
 *     <li>commentCount: 评论总数</li>
 * </ul>
 */
public class ListCommentsResponse {

	private Integer nextPageAnchor;

	@ItemType(CommentDTO.class)
	List<CommentDTO> commentDtos;

	private Long commentCount;

	public Integer getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Integer nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<CommentDTO> getCommentDtos() {
		return commentDtos;
	}

	public void setCommentDtos(List<CommentDTO> commentDtos) {
		this.commentDtos = commentDtos;
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