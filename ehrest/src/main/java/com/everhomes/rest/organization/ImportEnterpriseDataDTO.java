package com.everhomes.rest.organization;

import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;

import java.util.List;


public class ImportEnterpriseDataDTO {
	
	private String name = "";

	private String displayName = "";
	//人员规模
	private String memberRange = "";
	//管理员姓名
	private String adminName = "";
	//管理员手机号
	private String adminToken = "";

	private String email = "";

	private String buildingNameAndApartmentName;

	private String communityNames;
/*
	//办公地点楼栋
	private String buildingName = "";
	//办公地点门牌
	private String address = "";
*/
	//办公地点楼栋和门牌集合
	private List<OrganizationSiteApartmentDTO> siteDtos;

	//办公地点名称
	private String workPlaceName;
	//办公地点所属项目名称
	private String communityName;
	//是否属于管理公司标志
	private String pmFlag;
	//是否属于服务商标志
	private String serviceSupportFlag;
	//是否启用移动工作台
	private String workPlatFormFlag;
	//管理的项目名称集合
	private List<CommunityDTO> communityDTOList;

	private String contact = "";

	private String number = "";

	private String checkinDate = "";

	private String description = "";

	private String unifiedSocialCreditCode = "";

	public String getUnifiedSocialCreditCode() {
		return unifiedSocialCreditCode;
	}

	public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
		this.unifiedSocialCreditCode = unifiedSocialCreditCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminToken() {
		return adminToken;
	}

	public void setAdminToken(String adminToken) {
		this.adminToken = adminToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

/*	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}*/

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(String checkinDate) {
		this.checkinDate = checkinDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMemberRange() {
		return memberRange;
	}

	public void setMemberRange(String memberRange) {
		this.memberRange = memberRange;
	}

	public String getWorkPlaceName() {
		return workPlaceName;
	}

	public void setWorkPlaceName(String workPlaceName) {
		this.workPlaceName = workPlaceName;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getPmFlag() {
		return pmFlag;
	}

	public void setPmFlag(String pmFlag) {
		this.pmFlag = pmFlag;
	}

	public String getServiceSupportFlag() {
		return serviceSupportFlag;
	}

	public void setServiceSupportFlag(String serviceSupportFlag) {
		this.serviceSupportFlag = serviceSupportFlag;
	}

	public String getWorkPlatFormFlag() {
		return workPlatFormFlag;
	}

	public void setWorkPlatFormFlag(String workPlatFormFlag) {
		this.workPlatFormFlag = workPlatFormFlag;
	}

	public List<CommunityDTO> getCommunityDTOList() {
		return communityDTOList;
	}

	public void setCommunityDTOList(List<CommunityDTO> communityDTOList) {
		this.communityDTOList = communityDTOList;
	}

	public List<OrganizationSiteApartmentDTO> getSiteDtos() {
		return siteDtos;
	}

	public void setSiteDtos(List<OrganizationSiteApartmentDTO> siteDtos) {
		this.siteDtos = siteDtos;
	}

	public String getBuildingNameAndApartmentName() {
		return buildingNameAndApartmentName;
	}

	public void setBuildingNameAndApartmentName(String buildingNameAndApartmentName) {
		this.buildingNameAndApartmentName = buildingNameAndApartmentName;
	}

	public String getCommunityNames() {
		return communityNames;
	}

	public void setCommunityNames(String communityNames) {
		this.communityNames = communityNames;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
