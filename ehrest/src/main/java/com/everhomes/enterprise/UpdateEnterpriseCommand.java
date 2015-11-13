package com.everhomes.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.forum.AttachmentDescriptor;

/**
 * <ul>
 *  <li>id: 公司id</li>
 *  <li>name: 公司名称</li>
 *  <li>displayName: 公司简称</li>
 *  <li>avatar: 公司头像</li>
 *  <li>description: 公司描述</li>
 *  <li>addressId: 公司地址门牌号id列表</li>
 *  <li>attachments: 公司的附件信息</li>
 *  <li>communityId: 公司入驻园区id</li>
 *  <li>memberCount: 公司员工人数</li>
 *  <li>contactsPhone: 公司联系电话</li>
 *  <li>contactor: 公司联系人</li>
 *  <li>entries: 公司联系人电话</li>
 *  <li>enterpriseAddress: 公司地址</li>
 *  <li>enterpriseCheckinDate: 公司入驻时间</li>
 * </ul>
 *
 */
public class UpdateEnterpriseCommand {
	private Long id;
	private String   name;
    private String   displayName;
    private String   avatar;
    private String   description;
    private Long communityId;
    private Long memberCount;
    private String contactsPhone;
    private String contactor;
    private String entries;
    private String enterpriseAddress;
    private String enterpriseCheckinDate;
	@ItemType(Long.class)
    private List<Long> addressId;
    
    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;
    
    public String getEnterpriseCheckinDate() {
		return enterpriseCheckinDate;
	}
	public void setEnterpriseCheckinDate(String enterpriseCheckinDate) {
		this.enterpriseCheckinDate = enterpriseCheckinDate;
	}
	public String getEnterpriseAddress() {
		return enterpriseAddress;
	}
	public void setEnterpriseAddress(String enterpriseAddress) {
		this.enterpriseAddress = enterpriseAddress;
	}
	public Long getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(Long memberCount) {
		this.memberCount = memberCount;
	}
	public String getContactsPhone() {
		return contactsPhone;
	}
	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}
	public String getContactor() {
		return contactor;
	}
	public void setContactor(String contactor) {
		this.contactor = contactor;
	}
	public String getEntries() {
		return entries;
	}
	public void setEntries(String entries) {
		this.entries = entries;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public List<AttachmentDescriptor> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AttachmentDescriptor> attachments) {
		this.attachments = attachments;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
