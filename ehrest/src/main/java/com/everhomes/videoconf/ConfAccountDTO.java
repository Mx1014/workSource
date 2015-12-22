package com.everhomes.videoconf;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>enterpriseName: 公司名称</li>
 *  <li>department: 部门名称</li>
 *  <li>userId: 用户id</li>
 *  <li>userName: 用户名</li>
 *  <li>mobile: 手机号</li>
 *  <li>confType: 会议类型 0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话</li>
 *  <li>accountType: 账号类型 0-single 1-multiple</li>
 *  <li>updateDate: 更新时间</li>
 *  <li>status: 状态 0-invalid 1-valid 2-locked</li>
 * </ul>
 *
 */
public class ConfAccountDTO {
	
	private Long id;
	
	private Long userId;
	
	private String userName;
	
	private String mobile;
	
	private String enterpriseName;
	
	private String department;
	
	private Byte confType;
	
	private Byte accoutnType;
	
	private Timestamp updateDate;
	
	private Byte status;

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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Byte getConfType() {
		return confType;
	}

	public void setConfType(Byte confType) {
		this.confType = confType;
	}

	public Byte getAccoutnType() {
		return accoutnType;
	}

	public void setAccoutnType(Byte accoutnType) {
		this.accoutnType = accoutnType;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
