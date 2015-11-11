package com.everhomes.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.forum.AttachmentDescriptor;

/**
 * <ul>管理后台使用，应该使用批量添加实现
 *  <li>name: 公司名称</li>
 *  <li>displayName: 公司简称</li>
 *  <li>avatar: 公司头像</li>
 *  <li>description: 公司描述</li>
 *  <li>addressId: 公司地址门牌号id列表</li>
 *  <li>attachments: 公司的附件信息</li>
 * </ul>
 * @author janson
 *
 */
public class CreateEnterpriseCommand {
    private java.lang.String   name;
    private java.lang.String   displayName;
    private java.lang.String   avatar;
    private java.lang.String   description;
    
    @ItemType(Long.class)
    private List<Long> addressId;
    
    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;
    
    public List<AttachmentDescriptor> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AttachmentDescriptor> attachments) {
		this.attachments = attachments;
	}
	public java.lang.String getName() {
        return name;
    }
    public void setName(java.lang.String name) {
        this.name = name;
    }
    public java.lang.String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }
    public java.lang.String getAvatar() {
        return avatar;
    }
    public void setAvatar(java.lang.String avatar) {
        this.avatar = avatar;
    }
    public java.lang.String getDescription() {
        return description;
    }
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
	public List<Long> getAddressId() {
		return addressId;
	}
	public void setAddressId(List<Long> addressId) {
		this.addressId = addressId;
	}
    
}
