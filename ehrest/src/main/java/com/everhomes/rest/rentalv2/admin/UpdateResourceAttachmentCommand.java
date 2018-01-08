package com.everhomes.rest.rentalv2.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 给资源增加单元格
 * <li>rentalSiteId: 资源id</li> 
 * <li>attachments: 预约需要提交的信息 </li>
 * </ul>
 */
public class UpdateResourceAttachmentCommand {

	private String resourceType;
	@NotNull
	private Long rentalSiteId; 
	@ItemType(AttachmentConfigDTO.class)
	private List<AttachmentConfigDTO> attachments; 
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getRentalSiteId() {
		return rentalSiteId;
	}
	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}
	public List<AttachmentConfigDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AttachmentConfigDTO> attachments) {
		this.attachments = attachments;
	}

	 
	
}
