// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 公司id</li>
 * <li>attachment: 文件</li>
 * </ul>
 */
public class ImportPeriodSalaryCommand {

	private Long organizationId;

	private MultipartFile[] attachment;

	public ImportPeriodSalaryCommand() {

	}

	public ImportPeriodSalaryCommand(Long organizationId, MultipartFile[] attachment) {
		super();
		this.organizationId = organizationId;
		this.attachment = attachment;
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
