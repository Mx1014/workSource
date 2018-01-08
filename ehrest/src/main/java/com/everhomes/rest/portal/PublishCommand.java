// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间</li>
 * <li>contentType: 发布内容类型</li>
 * <li>contentId: 发布内容id</li>
 * <li>versionId: 发布版本id</li>
 * </ul>
 */
public class PublishCommand {

	private Integer namespaceId;

	private String contentType;

	private Long contentId;

	private Long versionId;

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
