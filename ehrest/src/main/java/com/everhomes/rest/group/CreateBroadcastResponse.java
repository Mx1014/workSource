// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>broadcastToken: id的标识</li>
 * <li>ownerType: 所属者，参考{@link com.everhomes.rest.group.BroadcastOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>title: 标题</li>
 * <li>contentType: 内容类型</li>
 * <li>content: 内容</li>
 * <li>contentAbstract: 摘要，暂无</li>
 * </ul>
 */
public class CreateBroadcastResponse {

	private String broadcastToken;

	private String ownerType;

	private Long ownerId;

	private String title;

	private String contentType;

	private String content;

	private String contentAbstract;

	public CreateBroadcastResponse() {

	}

	public CreateBroadcastResponse(String broadcastToken, String ownerType, Long ownerId, String title, String contentType, String content, String contentAbstract) {
		super();
		this.broadcastToken = broadcastToken;
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.title = title;
		this.contentType = contentType;
		this.content = content;
		this.contentAbstract = contentAbstract;
	}

	public String getBroadcastToken() {
		return broadcastToken;
	}

	public void setBroadcastToken(String broadcastToken) {
		this.broadcastToken = broadcastToken;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentAbstract() {
		return contentAbstract;
	}

	public void setContentAbstract(String contentAbstract) {
		this.contentAbstract = contentAbstract;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
