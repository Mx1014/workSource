// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>salaryGroupId: 薪酬批次id</li>
 * <li>emailContent: 邮件内容</li>
 * <li>salaryGroupEntitys: 字段项列表 参考{@link com.everhomes.rest.salarySalaryGroupEntityDTO}</li>
 * </ul>
 */
public class UpdateSalaryGroupEntitiesVisableCommand {

	private String ownerType;

	private Long ownerId;

	private Long salaryGroupId;

	private String emailContent;
	
	@ItemType(SalaryGroupEntityDTO.class)
	private List<SalaryGroupEntityDTO> salaryGroupEntitys;

	public UpdateSalaryGroupEntitiesVisableCommand() {

	}

	public UpdateSalaryGroupEntitiesVisableCommand(String ownerType, Long ownerId, Long salaryGroupId, List<SalaryGroupEntityDTO> salaryGroupEntitys) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.salaryGroupId = salaryGroupId;
		this.salaryGroupEntitys = salaryGroupEntitys;
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

	public List<SalaryGroupEntityDTO> getSalaryGroupEntitys() {
		return salaryGroupEntitys;
	}

	public void setSalaryGroupEntitys(List<SalaryGroupEntityDTO> salaryGroupEntitys) {
		this.salaryGroupEntitys = salaryGroupEntitys;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

}
