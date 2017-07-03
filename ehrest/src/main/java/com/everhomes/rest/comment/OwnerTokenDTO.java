// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 评论对象的id</li>
 * <li>type: 评论对象的类型 参考 {@link com.everhomes.rest.comment.OwnerType}</li>
 * </ul>
 */
public class OwnerTokenDTO {
	private Long id;
	private Byte type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}