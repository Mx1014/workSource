package com.everhomes.rest.community.admin;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.group.GroupMemberDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>userId: 用户id</li>
 *     <li>userName: 用户姓名</li>
 *     <li>nickName: 用户昵称</li>
 *     <li>communityId: 小区id</li>
 *     <li>isAuth: 认证状态  1认证 2未认证</li>
 *     <li>applyTime: 注册时间</li>
 *     <li>phone: 电话号码</li>
 *     <li>gender: 性别</li>
 *     <li>createTime: 注册时间</li>
 *     <li>executiveFlag: 是否高管 0-否 1-是</li>
 *     <li>position: 职位</li>
 *     <li>identityNumber: 身份证号</li>
 *     <li>orgDtos: 地址， 参考{@link com.everhomes.rest.organization.OrganizationDetailDTO}</li>
 *     <li>addressDtos: 地址， 参考{@link com.everhomes.rest.address.AddressDTO}</li>
 *     <li>memberLogDTOs: 用户认证记录， 参考{@link com.everhomes.rest.community.admin.OrganizationMemberLogDTO}</li>
 *     <li>userSourceType: userSourceType</li>
 *     <li>recentlyActiveTime: 最近活跃时间</li>
 *     <li>email: 邮箱</li>
 *     <li>vipLevel: 会员等级</li>
 *     <li>showVipLevelFlag: 是否展示会员等级，请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>groupmemberLogDTOs: 小区用户认证记录</li>
 * </ul>
 */
public class CommunityUserAddressDTO {

	private Long id;

	private Long userId;

	private String userName;

	private String nickName;

	private Long communityId;

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

	@ItemType(GroupMemberDTO.class)
	private List<GroupMemberDTO> groupmemberLogDTOs;

	private Byte userSourceType;

	private Long recentlyActiveTime;

	private String email;

	private Integer vipLevel;

	private Byte showVipLevelFlag;

    public List<GroupMemberDTO> getGroupmemberLogDTOs() {
        return groupmemberLogDTOs;
    }

    public void setGroupmemberLogDTOs(List<GroupMemberDTO> groupmemberLogDTOs) {
        this.groupmemberLogDTOs = groupmemberLogDTOs;
    }

    public Byte getShowVipLevelFlag() {
        return showVipLevelFlag;
    }

    public void setShowVipLevelFlag(Byte showVipLevelFlag) {
        this.showVipLevelFlag = showVipLevelFlag;
    }

    public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

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
