package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.organization.OrganizationAddressDTO;

/**
 * <ul>管理后台使用，应该使用批量添加实现
 *  <li>manageOrganizationId: 物业管理公司id</li>
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
 *  <li>postUri: 标题图</li>
 *  <li>longitude: 经度</li>
 *  <li>latitude: 纬度</li>
 *  <li>emailDomain: 邮箱域名 -非必填</li>
 *  <li>serviceUserId: 客服服务人员id</li>
 *  <li>website: 企业官网</li>
 *  <li>unifiedSocialCreditCode: 统一社会信用代码</li>
 * </ul>
 * @author janson
 *
 */

public class CreateEnterpriseCommand {
	private Boolean checkPrivilege;
	//物业管理公司id
	private Long manageOrganizationId;
	//公司名称
    private java.lang.String   name;
    //公司简称
    private java.lang.String   displayName;
    //
    private java.lang.String   avatar;
    private java.lang.String   description;
    private Long communityId;
    private Long memberCount;
    private String contactsPhone;
    private String contactor;
    private String entries;
    private String address;
    private String checkinDate;
    private String postUri;
    private Integer namespaceId;
    
    private String organizationType;
    
    private String longitude;
    
    private String latitude;
    private String emailDomain;
    private Long serviceUserId;
    
    @ItemType(OrganizationAddressDTO.class)
	private List<OrganizationAddressDTO> addressDTOs;
    
    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;

	private String website;
	private String unifiedSocialCreditCode;

	public Boolean getCheckPrivilege() {
		return checkPrivilege;
	}

	public void setCheckPrivilege(Boolean checkPrivilege) {
		this.checkPrivilege = checkPrivilege;
	}

	public Long getManageOrganizationId() {
		return manageOrganizationId;
	}

	public void setManageOrganizationId(Long manageOrganizationId) {
		this.manageOrganizationId = manageOrganizationId;
	}

	public String getUnifiedSocialCreditCode() {
		return unifiedSocialCreditCode;
	}

	public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
		this.unifiedSocialCreditCode = unifiedSocialCreditCode;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Long getServiceUserId() {
		return serviceUserId;
	}
	public void setServiceUserId(Long serviceUserId) {
		this.serviceUserId = serviceUserId;
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
    
	
	public List<OrganizationAddressDTO> getAddressDTOs() {
		return addressDTOs;
	}
	public void setAddressDTOs(List<OrganizationAddressDTO> addressDTOs) {
		this.addressDTOs = addressDTOs;
	}
	public String getPostUri() {
		return postUri;
	}
	public void setPostUri(String postUri) {
		this.postUri = postUri;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCheckinDate() {
		return checkinDate;
	}
	public void setCheckinDate(String checkinDate) {
		this.checkinDate = checkinDate;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getEmailDomain() {
		return emailDomain;
	}
	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}
    
}
