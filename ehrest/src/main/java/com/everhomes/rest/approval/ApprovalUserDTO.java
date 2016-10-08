// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>checkedFlag: 是否选中，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>departmentName: 部门名称</li>
 * <li>nickName: 姓名</li>
 * </ul>
 */
public class ApprovalUserDTO {
	private Byte checkedFlag;
	private String departmentName;
	private String nickName;

	public Byte getCheckedFlag() {
		return checkedFlag;
	}

	public void setCheckedFlag(Byte checkedFlag) {
		this.checkedFlag = checkedFlag;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
