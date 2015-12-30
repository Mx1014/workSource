package com.everhomes.rest.videoconf;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>updateTime: 更新时间</li>
 *  <li>enterpriseId: 企业id </li>
 *  <li>enterpriseName: 企业名称</li>
 *  <li>userId: 用户id</li>
 *  <li>userName: 用户名</li>
 *  <li>mobile: 手机号</li>
 *  <li>userType: 用户类型 </li>
 *  <li>status: 状态 0-invalid 1-valid 2-locked</li>
 *  <li>validDate: 有效期</li>
 *  <li>accountType: 账号类型 0-single 1-multiple</li>
 *  <li>confType: 会议类型 0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话</li>
 * </ul>
 *
 */
public class VideoConfAccountDTO {
	
	private Long id;
	
	private Timestamp updateTime;
	
	private Long userId;
	
	private String userName;
	
	private String mobile;
	
	private Long enterpriseId;
	
	private String enterpriseName;
	
	private String userType;
	
	private Timestamp validDate;
	
	private Byte status;
	
	private Byte accountType;
	
	private Byte confType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Timestamp getValidDate() {
		return validDate;
	}

	public void setValidDate(Timestamp validDate) {
		this.validDate = validDate;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getAccountType() {
		return accountType;
	}

	public void setAccountType(Byte accountType) {
		this.accountType = accountType;
	}

	public Byte getConfType() {
		return confType;
	}

	public void setConfType(Byte confType) {
		this.confType = confType;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
