package com.everhomes.rest.community.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>userId: 用户id</li>
 *     <li>userName: 用户姓名</li>
 *     <li>nickName: 用户昵称</li>
 *     <li>isAuth: 认证状态  1认证 2未认证</li>
 *     <li>applyTime: 注册时间</li>
 *     <li>phone: 电话号码</li>
 *     <li>gender: 性别</li>
 *     <li>createTime: 注册时间</li>
 *     <li>identityNumber: 身份证号</li>
 *     <li>orgDtos: 企业地址， 参考{@link OrganizationDetailDTO}</li>
 *     <li>addressDtos: 家庭地址， 参考{@link AddressDTO}</li>
 *     <li>memberLogDTOs: 用户认证记录， 参考{@link OrganizationMemberLogDTO}</li>
 *     <li>userSourceType: 用户来源 1：来源app 2：来源微信 3: 来源支付宝</li>
 *     <li>recentlyActiveTime: 最近活跃时间</li>
 *     <li>email: 邮箱</li>
 * </ul>
 */
public class CommunityAllUserDTO {

	private Long id;

	private Long userId;

	private String userName;

	private String nickName;

	private Integer isAuth;

	private Timestamp applyTime;

	private String phone;

	private Byte gender;

	private Long createTime;

	private String identityNumber;

	@ItemType(OrganizationDetailDTO.class)
	private List<OrganizationDetailDTO> orgDtos;

	@ItemType(AddressDTO.class)
	private List<AddressDTO> addressDtos;

	@ItemType(OrganizationMemberLogDTO.class)
	private List<OrganizationMemberLogDTO> memberLogDTOs;

	private Byte userSourceType;

	private Long recentlyActiveTime;

	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public Byte getUserSourceType() {
		return userSourceType;
	}

	public void setUserSourceType(Byte userSourceType) {
		this.userSourceType = userSourceType;
	}

	public Long getRecentlyActiveTime() {
		return recentlyActiveTime;
	}

	public void setRecentlyActiveTime(Long recentlyActiveTime) {
		this.recentlyActiveTime = recentlyActiveTime;
	}
}
