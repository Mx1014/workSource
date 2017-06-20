// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>salaryGroupId: 薪酬批次id</li>
 * <li>organizationId: 公司id</li>
 * <li>attachment: 文件</li>
 * </ul>
 */
public class ImportSalaryGroupCommand {

	private Long salaryGroupId;

	private Long organizationId;

	private MultipartFile[] attachment;

	public ImportSalaryGroupCommand() {

	}

	public ImportSalaryGroupCommand(Long salaryGroupId, Long organizationId, MultipartFile[] attachment) {
		super();
		this.salaryGroupId = salaryGroupId;
		this.organizationId = organizationId;
		this.attachment = attachment;
	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public MultipartFile[] getAttachment() {
		return attachment;
	}

	public void setAttachment(MultipartFile[] attachment) {
		this.attachment = attachment;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
