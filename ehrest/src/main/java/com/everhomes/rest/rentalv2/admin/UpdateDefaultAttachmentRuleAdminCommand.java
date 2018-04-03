package com.everhomes.rest.rentalv2.admin;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 添加默认规则
 * <li>ownerType: 所有者类型 参考
 * {@link com.everhomes.rest.rentalv2.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceTypeId: 图标id</li> 
 * <li>attachments: 预约需要提交的信息{@link com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO}</li> 
 * </ul>
 */
public class UpdateDefaultAttachmentRuleAdminCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Long resourceTypeId; 
	@ItemType(AttachmentConfigDTO.class)
	private List<AttachmentConfigDTO> attachments; 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public List<AttachmentConfigDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentConfigDTO> attachments) {
		this.attachments = attachments;
	} 

	
}
