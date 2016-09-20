package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>requestJson:申请信息json</li>
 *  <li>templateType:模板类型</li>
 *  <li>ownerType:拥有者类型：现在是comunity </li>
 *  <li>ownerId:拥有者id</li>
 *  <li>type:服务联盟机构类型</li>
 *  <li>categoryId:服务联盟机构类型id</li>
 *  <li>creatorOrganizationId:创建者所在机构id</li>
 *  <li>serviceAllianceId:服务联盟机构id</li>
 *  <li>attachments:附件列表 参考{@link com.everhomes.rest.user.RequestAttachmentsDTO}</li>
 * </ul>
 */
public class AddRequestCommand {

	private String requestJson;
	
	private String templateType;

	private String ownerType;
	
	private Long ownerId;
	
	private Long type;
	
	private Long categoryId;
	
	private Long creatorOrganizationId;
	
	private Long serviceAllianceId;
	
	@ItemType(RequestAttachmentsDTO.class)
	private List<RequestAttachmentsDTO> attachments;


	public String getRequestJson() {
		return requestJson;
	}


	public void setRequestJson(String requestJson) {
		this.requestJson = requestJson;
	}


	public String getTemplateType() {
		return templateType;
	}


	public void setTemplateType(String templateType) {
		this.templateType = templateType;
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


	public Long getType() {
		return type;
	}


	public void setType(Long type) {
		this.type = type;
	}


	public Long getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}


	public Long getCreatorOrganizationId() {
		return creatorOrganizationId;
	}


	public void setCreatorOrganizationId(Long creatorOrganizationId) {
		this.creatorOrganizationId = creatorOrganizationId;
	}


	public Long getServiceAllianceId() {
		return serviceAllianceId;
	}


	public void setServiceAllianceId(Long serviceAllianceId) {
		this.serviceAllianceId = serviceAllianceId;
	}


	public List<RequestAttachmentsDTO> getAttachments() {
		return attachments;
	}


	public void setAttachments(List<RequestAttachmentsDTO> attachments) {
		this.attachments = attachments;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
