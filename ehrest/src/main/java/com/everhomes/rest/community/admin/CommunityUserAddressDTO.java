package com.everhomes.rest.community.admin;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;

public class CommunityUserAddressDTO {

	private Long     id;
	
	private Long     userId;
	
	private String     userName;
	
	private String     nikeName;
	
	private Long     communityId;
	
	private Integer isAuth;
	
	private Timestamp applyTime;
	
	private String phone;
	
	private Byte gender;
	
	private List<OrganizationDetailDTO> orgDtos;
	
	private List<AddressDTO> addressDtos;
	
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

	public List<OrganizationDetailDTO> getOrgDtos() {
		return orgDtos;
	}

	public void setOrgDtos(List<OrganizationDetailDTO> orgDtos) {
		this.orgDtos = orgDtos;
	}

	public List<AddressDTO> getAddressDtos() {
		return addressDtos;
	}

	public void setAddressDtos(List<AddressDTO> addressDtos) {
		this.addressDtos = addressDtos;
	}

	
}
