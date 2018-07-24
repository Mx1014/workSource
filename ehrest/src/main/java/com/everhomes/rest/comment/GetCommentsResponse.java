// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *     <li>comment: 评论，参考 {@link com.everhomes.rest.comment.CommentDTO}</li>
 *     <li>commentCount: 评论总数</li>
 * </ul>
 */
public class GetCommentsResponse {

	private CommentDTO comment;

	private Long commentCount;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public CommentDTO getComment() {
		return comment;
	}

	public void setComment(CommentDTO comment) {
		this.comment = comment;
	}

}