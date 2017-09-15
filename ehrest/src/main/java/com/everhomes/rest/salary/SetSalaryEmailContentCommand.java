// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>salaryGroupId: 薪酬批次id 如果为空就是给全局设置</li>
 * <li>emailContent: 邮件内容</li>
 * </ul>
 */
public class SetSalaryEmailContentCommand {

	private String ownerType;

	private Long ownerId;

	private Long salaryGroupId;

	private String emailContent;

	public SetSalaryEmailContentCommand() {

	}

	public SetSalaryEmailContentCommand(String ownerType, Long ownerId, Long salaryGroupId, String emailContent) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.salaryGroupId = salaryGroupId;
		this.emailContent = emailContent;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
