package com.everhomes.rest.organization;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.admin.CommunityUserOrgDetailDTO;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 主键id</li>
 *     <li>organizationId: 机构id</li>
 *     <li>description: 描述</li>
 *     <li>contact: 联系电话</li>
 *     <li>address: 地址</li>
 *     <li>geohash: 地理哈希</li>
 *     <li>displayName: displayName</li>
 *     <li>contactor: 联系人</li>
 *     <li>memberCount: 机构人数</li>
 *     <li>checkinDate: 入住时间</li>
 *     <li>name: 机构名称</li>
 *     <li>avatarUri: 机构logo uri</li>
 *     <li>avatarUrl: 机构logo url</li>
 *     <li>updateTime: updateTime</li>
 *     <li>createTime: createTime</li>
 *     <li>accountPhone: 公司管理账号电话</li>
 *     <li>accountName: 公司管理账号人名称</li>
 *     <li>assignmentId: 用户权限id</li>
 *     <li>postUri: 标题图 uri</li>
 *     <li>postUrl: 标题图 url</li>
 *     <li>longitude: 经度</li>
 *     <li>latitude: 纬度</li>
 *     <li>member: member {@link com.everhomes.rest.organization.OrganizationMemberDTO}</li>
 *     <li>community: community {@link com.everhomes.rest.address.CommunityDTO}</li>
 *     <li>emailDomain: 邮箱域名</li>
 *     <li>addresses: 机构入住门牌地址 {@link com.everhomes.rest.address.AddressDTO}</li>
 *     <li>attachments: 机构banner图 {@link com.everhomes.rest.forum.AttachmentDescriptor}</li>
 *     <li>communityId: communityId</li>
 *     <li>communityName: communityName</li>
 *     <li>signupCount: 注册人数</li>
 *     <li>serviceUserId: 客服服务人员id</li>
 *     <li>serviceUserName: 客服服务人员名称</li>
 *     <li>serviceUserPhone: 客服服务人员电话</li>
 *     <li>adminMembers: 管理员列表，参考{@link com.everhomes.rest.organization.OrganizationContactDTO}</li>
 *     <li>website: 企业官网</li>
 *     <li>unifiedSocialCreditCode: 统一社会信用代码</li>
 *     <li>communityUserOrgDetailDTO: 用户企业信息 {@link CommunityUserOrgDetailDTO}</li>
 *     <li>organizationMemberName: 当前用户在企业的contact_name</li>
 * </ul>
 */
public class  OrganizationDetailDTO {

	private Long id;
	private Long organizationId;
	private String description;
	private String contact;
	private String address;
	private String geohash;
	private String displayName;
	private String contactor;
	private Long memberCount;
	private Long checkinDate;
	private String name;

	private String avatarUri;
	private String avatarUrl;

	private Long updateTime;
	private Long createTime;

	private String accountPhone;
	private String accountName;
	private Long assignmentId;

	private String postUri;
	private String postUrl;

	private String longitude;

	private String latitude;

	private OrganizationMemberDTO member;

	private CommunityDTO community;

	private String emailDomain;
	//TODO address info ?
	@ItemType(value = AddressDTO.class)
	private List<AddressDTO> addresses;

	@ItemType(value = AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachments = new ArrayList<AttachmentDescriptor>();

	private Long communityId;

	private String communityName;

	private Integer signupCount;

	private Long serviceUserId;

	private String serviceUserName;

	private String serviceUserPhone;

	@ItemType(OrganizationMemberDTO.class)
	private List<OrganizationContactDTO> adminMembers;

	private String website;

	private String unifiedSocialCreditCode;

	private CommunityUserOrgDetailDTO communityUserOrgDetailDTO;

	private String organizationMemberName;

	private Boolean thirdPartFlag = false;

	public String getUnifiedSocialCreditCode() {
		return unifiedSocialCreditCode;
	}

	public Boolean getThirdPartFlag() {
		return thirdPartFlag;
	}

	public void setThirdPartFlag(Boolean thirdPartFlag) {
		this.thirdPartFlag = thirdPartFlag;
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

	public Integer getSignupCount() {
		return signupCount;
	}


	public void setSignupCount(Integer signupCount) {
		this.signupCount = signupCount;
	}


	public List<OrganizationContactDTO> getAdminMembers() {
		return adminMembers;
	}


	public void setAdminMembers(List<OrganizationContactDTO> adminMembers) {
		this.adminMembers = adminMembers;
	}


	public Long getServiceUserId() {
		return serviceUserId;
	}


	public void setServiceUserId(Long serviceUserId) {
		this.serviceUserId = serviceUserId;
	}


	public String getServiceUserName() {
		return serviceUserName;
	}


	public void setServiceUserName(String serviceUserName) {
		this.serviceUserName = serviceUserName;
	}


	public String getServiceUserPhone() {
		return serviceUserPhone;
	}


	public void setServiceUserPhone(String serviceUserPhone) {
		this.serviceUserPhone = serviceUserPhone;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getContact() {
		return contact;
	}


	public void setContact(String contact) {
		this.contact = contact;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getGeohash() {
		return geohash;
	}


	public void setGeohash(String geohash) {
		this.geohash = geohash;
	}


	public String getDisplayName() {
		return displayName;
	}


	public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getContactor() {
		return contactor;
	}


	public void setContactor(String contactor) {
		this.contactor = contactor;
	}


	public Long getMemberCount() {
		return memberCount;
	}


	public void setMemberCount(Long memberCount) {
		this.memberCount = memberCount;
	}


	public Long getCheckinDate() {
		return checkinDate;
	}


	public void setCheckinDate(Long checkinDate) {
		this.checkinDate = checkinDate;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAvatarUri() {
		return avatarUri;
	}


	public void setAvatarUri(String avatarUri) {
		this.avatarUri = avatarUri;
	}


	public String getAvatarUrl() {
		return avatarUrl;
	}


	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}


	public Long getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}


	public Long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}


	public String getAccountPhone() {
		return accountPhone;
	}


	public void setAccountPhone(String accountPhone) {
		this.accountPhone = accountPhone;
	}


	public String getAccountName() {
		return accountName;
	}


	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}


	public String getPostUri() {
		return postUri;
	}


	public void setPostUri(String postUri) {
		this.postUri = postUri;
	}


	public String getPostUrl() {
		return postUrl;
	}


	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}


	public List<AddressDTO> getAddresses() {
		return addresses;
	}


	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}


	public List<AttachmentDescriptor> getAttachments() {
		return attachments;
	}


	public void setAttachments(List<AttachmentDescriptor> attachments) {
		this.attachments = attachments;
	}


	public Long getAssignmentId() {
		return assignmentId;
	}


	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}


	public OrganizationMemberDTO getMember() {
		return member;
	}


	public void setMember(OrganizationMemberDTO member) {
		this.member = member;
	}


	public CommunityDTO getCommunity() {
		return community;
	}


	public void setCommunity(CommunityDTO community) {
		this.community = community;
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

	public CommunityUserOrgDetailDTO getCommunityUserOrgDetailDTO() {
		return communityUserOrgDetailDTO;
	}

	public void setCommunityUserOrgDetailDTO(CommunityUserOrgDetailDTO communityUserOrgDetailDTO) {
		this.communityUserOrgDetailDTO = communityUserOrgDetailDTO;
	}

	public String getOrganizationMemberName() {
		return organizationMemberName;
	}

	public void setOrganizationMemberName(String organizationMemberName) {
		this.organizationMemberName = organizationMemberName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public String getEmailDomain() {
		return emailDomain;
	}


	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		OrganizationDetailDTO that = (OrganizationDetailDTO) o;

		return id != null ? id.equals(that.id) : that.id == null;

	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
