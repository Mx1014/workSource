// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerToken: 实体标识</li>
 * <li>id: 评论id</li>
 * </ul>
 */
public class DeleteCommonCommentCommand {
	@NotNull
	private String ownerToken;
	private Long id;

	public String getOwnerToken() {
		return ownerToken;
	}

	public void setOwnerToken(String ownerToken) {
		this.ownerToken = ownerToken;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}