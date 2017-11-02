package com.everhomes.rest.community.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>userId: userId</li>
 *     <li>userName: userName</li>
 *     <li>nikeName: nikeName</li>
 *     <li>organizationMemberName: organizationMemberName</li>
 *     <li>communityId: communityId</li>
 *     <li>isAuth: isAuth</li>
 *     <li>enterpriseName: enterpriseName</li>
 *     <li>buildingId: buildingId</li>
 *     <li>buildingName: buildingName</li>
 *     <li>addressId: addressId</li>
 *     <li>addressName: addressName</li>
 *     <li>applyTime: 申请时间</li>
 *     <li>phone: phone</li>
 *     <li>executiveFlag: 是否高管 0-否 1-是</li>
 *     <li>position: 职位</li>
 *     <li>identityNumber: 身份证号</li>
 *     <li>gender: 性别</li>
 *     <li>recentlyActiveTime: recentlyActiveTime</li>
 *     <li>userSourceType: userSourceType</li>
 *     <li>organizations: organizations {@link com.everhomes.rest.organization.OrganizationDetailDTO}</li>
 * </ul>
 */
public class CommunityUserDto {

	private Long id;

	private Long userId;

	private String userName;

	private String nikeName;

	private String organizationMemberName;

	private Long communityId;

	private Integer isAuth;

	private String enterpriseName;

	private String buildingId;

	private String buildingName;

	private Long addressId;

	private String addressName;

	private Timestamp applyTime;

	private String phone;

	private Byte executiveFlag;
	private String position;
	private String identityNumber;
	private Byte gender;

	private Long recentlyActiveTime;

	private Byte userSourceType;

	@ItemType(OrganizationDetailDTO.class)
	private List<OrganizationDetailDTO> organizations;

	public List<OrganizationDetailDTO> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<OrganizationDetailDTO> organizations) {
		this.organizations = organizations;
	}

	public Byte getExecutiveFlag() {
		return executiveFlag;
	}

	public void setExecutiveFlag(Byte executiveFlag) {
		this.executiveFlag = executiveFlag;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Integer getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public Timestamp getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public Long getRecentlyActiveTime() {
		return recentlyActiveTime;
	}

	public void setRecentlyActiveTime(Long recentlyActiveTime) {
		this.recentlyActiveTime = recentlyActiveTime;
	}

	public String getOrganizationMemberName() {
		return organizationMemberName;
	}

	public void setOrganizationMemberName(String organizationMemberName) {
		this.organizationMemberName = organizationMemberName;
	}

	public Byte getUserSourceType() {
		return userSourceType;
	}

	public void setUserSourceType(Byte userSourceType) {
		this.userSourceType = userSourceType;
	}
}
