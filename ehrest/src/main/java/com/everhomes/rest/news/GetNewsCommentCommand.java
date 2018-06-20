/**
 * 
 */
package com.everhomes.rest.news;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>commentId: 评论id</li>
 * </ul>
 * @author 黄明波
 */
public class GetNewsCommentCommand {
	private Long commentId;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
}
