package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>organizationId: 机构id</li>
 *  <li>description: 描述</li>
 *  <li>contact: 联系电话</li>
 *  <li>address: 地址</li>
 *  <li>geohash: 地理哈希</li>
 *  <li>contactor: 联系人</li>
 *  <li>memberCount: 机构人数</li>
 *  <li>checkinDate: 入住时间</li>
 *  <li>name: 机构名称</li>
 *  <li>avatarUri: 机构logo uri</li>
 *  <li>avatarUrl: 机构logo url</li>
 *  <li>accountPhone: 公司管理账号电话</li>
 *  <li>accountName: 公司管理账号人名称</li>
 *  <li>assignmentId: 用户权限id</li>
 *  <li>postUri: 标题图 uri</li>
 *  <li>postUrl: 标题图 url</li>
 *  <li>addresses: 机构入住门牌地址</li>
 *  <li>attachments: 机构banner图</li>
 *  <li>longitude: 经度</li>
 *  <li>latitude: 纬度</li>
 *  <li>emailDomain: 邮箱域名 </li>
 *  <li>signupCount: 注册人数</li>
 *  <li>serviceUserId: 客服服务人员id</li>
 *  <li>serviceUserName: 客服服务人员名称</li>
 *  <li>serviceUserPhone: 客服服务人员电话</li>
 *  <li>adminMembers: 管理员列表，参考{@link com.everhomes.rest.organization.OrganizationContactDTO}</li>
 * </ul>
 *
 */
public class OrganizationExportDetailDTO {

	private String displayName;
	private String emailDomain;
	@ItemType(value = AddressDTO.class)
	private List<AddressDTO> addresses;
	private String apartments;
	private Integer signupCount;
	private Long memberCount;
	private String serviceUserName;
	@ItemType(OrganizationMemberDTO.class)
	private List<OrganizationContactDTO> adminMembers;
	private String admins;
	private String address;
	private String contact;
	private Long checkinDate;
	private String checkinDateString;
	private String description;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmailDomain() {
		return emailDomain;
	}

	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}

	public List<AddressDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}

	public String getApartments() {
		return apartments;
	}

	public void setApartments(String apartments) {
		this.apartments = apartments;
	}

	public Integer getSignupCount() {
		return signupCount;
	}

	public void setSignupCount(Integer signupCount) {
		this.signupCount = signupCount;
	}

	public Long getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Long memberCount) {
		this.memberCount = memberCount;
	}

	public String getServiceUserName() {
		return serviceUserName;
	}

	public void setServiceUserName(String serviceUserName) {
		this.serviceUserName = serviceUserName;
	}

	public List<OrganizationContactDTO> getAdminMembers() {
		return adminMembers;
	}

	public void setAdminMembers(List<OrganizationContactDTO> adminMembers) {
		this.adminMembers = adminMembers;
	}

	public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Long getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(Long checkinDate) {
		this.checkinDate = checkinDate;
	}

	public String getCheckinDateString() {
		return checkinDateString;
	}

	public void setCheckinDateString(String checkinDateString) {
		this.checkinDateString = checkinDateString;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
