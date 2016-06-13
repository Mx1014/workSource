// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：政府机构id</li>
 * <li>accountPhone：账号电话号码</li>
 * <li>accountName：姓名</li>
 * </ul>
 */
public class CreateOrganizationAccountCommand {
	@NotNull
	private Long    organizationId;
	
	@NotNull
	private String accountPhone;
	
	private String  accountName;
	
	private Long assignmentId;
	
	public CreateOrganizationAccountCommand() {
    }

	


	public Long getOrganizationId() {
		return organizationId;
	}




	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}





	public String getAccountPhone() {
		return accountPhone;
	}




	public void setAccountPhone(String accountPhone) {
		this.accountPhone = accountPhone;
	}




	public String getAccountName() {
		return accountName;
	}




	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}




	public Long getAssignmentId() {
		return assignmentId;
	}




	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}




	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
