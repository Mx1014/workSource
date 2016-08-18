package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>id: 任务ID</li>
 * <li>content: 完成或关闭任务理由</li>
 * <li>attachments: 图片路径列表</li>
 * </ul>
 */
public class CompleteTaskCommand {
	private String ownerType;
    private Long ownerId;
    private Long id;
    private String content;
	@ItemType(String.class)
	private List<String> attachments;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<String> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
