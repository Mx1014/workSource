// @formatter:off
package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id</li>
 * <li>detailIds: 员工detailid 列表</li>
 * <li>annualLeaveBalanceCorrection: 年假余额调整值 正数加,负数减</li>
 * <li>overtimeCompensationBalanceCorrection: 调休余额调整值 正数加,负数减</li>
 * <li>description: 备注</li>
 * </ul>
 */
public class BatchUpdateVacationBalancesCommand {

	private Long organizationId;

	@ItemType(Long.class)
	private List<Long> detailIds;

	private Double annualLeaveBalanceCorrection;

	private Double overtimeCompensationBalanceCorrection;

	private String description;

	public BatchUpdateVacationBalancesCommand() {

	}

	public BatchUpdateVacationBalancesCommand(Long organizationId, List<Long> detailIds, Double annualLeaveBalanceCorrection, Double overtimeCompensationBalanceCorrection, String description) {
		super();
		this.organizationId = organizationId;
		this.detailIds = detailIds;
		this.annualLeaveBalanceCorrection = annualLeaveBalanceCorrection;
		this.overtimeCompensationBalanceCorrection = overtimeCompensationBalanceCorrection;
		this.description = description;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public List<Long> getDetailIds() {
		return detailIds;
	}

	public void setDetailIds(List<Long> detailIds) {
		this.detailIds = detailIds;
	}

	public Double getAnnualLeaveBalanceCorrection() {
		return annualLeaveBalanceCorrection;
	}

	public void setAnnualLeaveBalanceCorrection(Double annualLeaveBalanceCorrection) {
		this.annualLeaveBalanceCorrection = annualLeaveBalanceCorrection;
	}

	public Double getOvertimeCompensationBalanceCorrection() {
		return overtimeCompensationBalanceCorrection;
	}

	public void setOvertimeCompensationBalanceCorrection(Double overtimeCompensationBalanceCorrection) {
		this.overtimeCompensationBalanceCorrection = overtimeCompensationBalanceCorrection;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
