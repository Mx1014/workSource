package com.everhomes.community;

import java.sql.Timestamp;

import com.everhomes.rest.community.admin.CommunityUserResponse;
import com.everhomes.rest.community.admin.ListUserCommunitiesCommand;
import com.everhomes.server.schema.tables.pojos.EhUserCommunities;

public class CommunityUser extends EhUserCommunities {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1558176514465214471L;
	
	private Long userId;
	
	private String userName;
	
	private String phone;
	
	private String enterpriseName;
	
	private String buildingId;
	
	private String buildingName;
	
	private Long addressId;
	
	private String addressName;
	
	private Timestamp applyTime;
	
	private Integer isAuth;

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

	public Integer getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
