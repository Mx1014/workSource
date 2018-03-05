package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;

import java.util.List;

/**
 * <ul>
 * <li>attachments: 预约需要提交的信息</li>
 * <li>ownerType: 默认规则:EhRentalv2DefaultRules 资源规则:EhRentalv2Resources</li>
 * <li>ownerId: 预约需要提交的信息</li>
 * </ul>
 */
public class ResourceAttachmentDTO {

    private String ownerType;

    private Long ownerId;

    @ItemType(AttachmentConfigDTO.class)
    private List<AttachmentConfigDTO> attachments;

    public List<AttachmentConfigDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentConfigDTO> attachments) {
        this.attachments = attachments;
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
}
