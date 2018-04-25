// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id</li>
 * <li>ownerId: 分公司id</li>
 * <li>salaryPeriod: 期数:类似201808</li>
 * <li>details: 导入工资条详情 {@link com.everhomes.rest.salary.PayslipDetailDTO}</li>
 * </ul>
 */
public class SendPayslipCommand {

	private Long organizationId;

	private Long ownerId;

	private String salaryPeriod;

	@ItemType(PayslipDetailDTO.class)
	private List<PayslipDetailDTO> details;

	public SendPayslipCommand() {

	}

	public SendPayslipCommand(Long organizationId, Long ownerId, String salaryPeriod, List<PayslipDetailDTO> details) {
		super();
		this.organizationId = organizationId;
		this.ownerId = ownerId;
		this.salaryPeriod = salaryPeriod;
		this.details = details;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getSalaryPeriod() {
		return salaryPeriod;
	}

	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}

	public List<PayslipDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<PayslipDetailDTO> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
