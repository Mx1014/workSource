package com.everhomes.rest.videoconf;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
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
 *  <li>validDate: 有效期</li>
 *  <li>userType: 用户类型  0-试用用户  1-新增用户  2-续约用户</li>
 *  <li>validFlag: 有效状态  0-过期  1-有效 2-即将过期（1个月内） </li>
 *  <li>status: 状态 0-invalid 1-valid 2-locked</li>
 *  <li>category: 参考com.everhomes.rest.videoconf.ConfCategoryDTO</li>
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
	
	private Byte accountType;
	
	private Timestamp updateDate;
	
	private Timestamp validDate;
	
	private Byte userType;
	
	private Byte validFlag;
	
	private Byte status;
	
	@ItemType(ConfCategoryDTO.class)
	private ConfCategoryDTO category;

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

	public Byte getAccountType() {
		return accountType;
	}

	public void setAccountType(Byte accountType) {
		this.accountType = accountType;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Timestamp getValidDate() {
		return validDate;
	}

	public void setValidDate(Timestamp validDate) {
		this.validDate = validDate;
	}

	public Byte getUserType() {
		return userType;
	}

	public void setUserType(Byte userType) {
		this.userType = userType;
	}

	public Byte getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(Byte validFlag) {
		this.validFlag = validFlag;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public ConfCategoryDTO getCategory() {
		return category;
	}

	public void setCategory(ConfCategoryDTO category) {
		this.category = category;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
