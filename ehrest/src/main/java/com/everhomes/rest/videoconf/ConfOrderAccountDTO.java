package com.everhomes.rest.videoconf;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>departmentId: 部门id </li>
 *  <li>department: 部门名称</li>
 *  <li>userId: 用户id</li>
 *  <li>userName: 用户名</li>
 *  <li>mobile: 手机号</li>
 *  <li>confType: 会议类型 0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话</li>
 * </ul>
 *
 */
public class ConfOrderAccountDTO {
	
	private Long id;
	
	private Long userId;
	
	private String userName;
	
	private String mobile;
	
	private Long departmentId;
	
	private String department;
	
	private Byte confType;

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

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
