/**
 * 
 */
package com.everhomes.rest.news;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>comment: 评论实体</li>
 * <li>commentCount: 评论数目</li>
 * </ul>
 * @author 黄明波
 */
public class GetNewsCommentResponse {
	
	private NewsCommentDTO comment;
	
	private Long commentCount;

	public NewsCommentDTO getComment() {
		return comment;
	}

	public void setComment(NewsCommentDTO comment) {
		this.comment = comment;
	}
	
	
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
}
