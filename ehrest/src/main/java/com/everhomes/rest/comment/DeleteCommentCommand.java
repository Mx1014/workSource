// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>sceneToken: 场景标识</li>
 * <li>sourceType: 评论来源，如快讯、论坛等, 参考{@link com.everhomes.rest.comment.SourceType}</li>
 * <li>entityToken: 实体标识</li>
 * <li>entityId: 实体ID</li>
 * <li>id: 评论id</li>
 * </ul>
 */
public class DeleteCommentCommand {
	private String sceneToken;
	@NotNull
	private Byte sourceType;
	private String entityToken;
	private Long entityId;
	private Long id;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public Byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(Byte sourceType) {
		this.sourceType = sourceType;
	}

	public String getEntityToken() {
		return entityToken;
	}

	public void setEntityToken(String entityToken) {
		this.entityToken = entityToken;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
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