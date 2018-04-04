// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>contentType: 发布内容类型</li>
 *     <li>contentId: 发布内容id</li>
 *     <li>versionId: 发布版本id</li>
 *     <li>publishType: 发布类型 1-预览，2-正式发布</li>
 *     <li>versionUserIds: 预览版本用户ids</li>
 * </ul>
 */
public class PublishCommand {

	private Integer namespaceId;

	private String contentType;

	private Long contentId;

	private Long versionId;

	private Byte publishType;

	private List<Long> versionUserIds;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	public Byte getPublishType() {
		return publishType;
	}

	public void setPublishType(Byte publishType) {
		this.publishType = publishType;
	}

	public List<Long> getVersionUserIds() {
		return versionUserIds;
	}

	public void setVersionUserIds(List<Long> versionUserIds) {
		this.versionUserIds = versionUserIds;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
