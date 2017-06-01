// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerType: 评论来源，如快讯、论坛等, 参考{@link com.everhomes.rest.comment.OwnerType}</li>
 * <li>ownerToken: 实体标识</li>
 * <li>id: 评论id</li>
 * </ul>
 */
public class DeleteCommentCommand {
	@NotNull
	private Byte ownerType;
	private String ownerToken;
	private Long id;

	public Byte getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
	}

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