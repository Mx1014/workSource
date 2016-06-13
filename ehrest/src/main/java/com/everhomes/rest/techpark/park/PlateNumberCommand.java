package com.everhomes.rest.techpark.park;

/**
 * <ul>
 *  <li>userId: 用户id</li>
 *  <li>userName: 用户名</li>
 *  <li>phoneNumber: 手机号</li>
 *  <li>companyName: 公司名</li>
 *  <li>plateNumber: 车牌号</li>
 *  <li>communityId: 园区id</li>
 * </ul>
 */
public class PlateNumberCommand {

	private Long communityId;
	
	private String plateNumber;
	
	private String companyName;
	
	private String phoneNumber;
	
	private String userName;
	
	private Long userId;
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
}
