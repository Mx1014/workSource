// @formatter:off
package com.everhomes.department;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>departmentId：政府id</li>
 * <li>memberUid：用户id</li>
 * <li>memberGroup：成员类型。参考 {@link com.everhomes.department.DepartmentGroup}</li>
 * </ul>
 */
public class CreateDepartmentMemberCommand {
	
	private Long   departmentId;
	private Long   memberUid;
	private String memberGroup;
	
	public CreateDepartmentMemberCommand() {
    }
	
	
	public Long getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}


	public Long getMemberUid() {
		return memberUid;
	}


	public void setMemberUid(Long memberUid) {
		this.memberUid = memberUid;
	}


	public String getMemberGroup() {
		return memberGroup;
	}


	public void setMemberGroup(String memberGroup) {
		this.memberGroup = memberGroup;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
