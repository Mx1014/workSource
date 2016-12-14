package com.everhomes.rest.community.admin;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;

/**
 * <ul>
 * <li>userId: 用户id</li>
 * <li>userName: 用户姓名</li>
 * <li>nikeName: 用户昵称</li>
 * <li>communityId: 小区id</li>
 * <li>isAuth: 认证状态  1认证 2未认证</li>
 * <li>applyTime: 时间</li>
 * <li>phone: 电话号码</li>
 * <li>gender: 性别</li>
 * <li>addressDtos: 地址， 参考{@link com.everhomes.rest.address.AddressDTO}</li>
 * <li>orgDtos: 地址， 参考{@link com.everhomes.rest.organization.OrganizationDetailDTO}</li>
 * <li>createTime：注册时间</li>
 * <li>executiveFlag：是否高管 0-否 1-是</li>
 * <li>position：职位</li>
 * <li>identityNumber：身份证号</li> 
 * <li>memberLogDTOs: 用户认证记录， 参考{@link com.everhomes.rest.community.admin.OrganizationMemberLogDTO}</li>
 * </ul>
 */
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

	private Long createTime; 

    private Byte executiveFlag;
    private String position;
    private String identityNumber;
	
	@ItemType(OrganizationDetailDTO.class)
	private List<OrganizationDetailDTO> orgDtos;
	
	@ItemType(AddressDTO.class)
	private List<AddressDTO> addressDtos;

	@ItemType(OrganizationMemberLogDTO.class)
	private List<OrganizationMemberLogDTO> memberLogDTOs;
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
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

	public List<OrganizationMemberLogDTO> getMemberLogDTOs() {
		return memberLogDTOs;
	}

	public void setMemberLogDTOs(List<OrganizationMemberLogDTO> memberLogDTOs) {
		this.memberLogDTOs = memberLogDTOs;
	}

	
}
