// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>sourceType: 评论来源，如快讯、论坛等, 参考{@link com.everhomes.rest.comment.SourceType}</li>
 * <li>entityToken: 实体标识</li>
 * <li>entityId: 实体ID</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListCommentsCommand {
	@NotNull
	private Byte sourceType;
	private String entityToken;
	private Long entityId;
	private Long pageAnchor;
	private Integer pageSize;

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

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}