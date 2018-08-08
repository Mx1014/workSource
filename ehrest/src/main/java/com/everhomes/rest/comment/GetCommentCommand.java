/**
 * 
 */
package com.everhomes.rest.comment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerToken: 实体标识</li>
 * <li>commentId: 评论id</li>
 * </ul>
 */
public class GetCommentCommand {
	@NotNull
	private String ownerToken;
	
	@NotNull
	private Long commentId;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getOwnerToken() {
		return ownerToken;
	}

	public void setOwnerToken(String ownerToken) {
		this.ownerToken = ownerToken;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
}
