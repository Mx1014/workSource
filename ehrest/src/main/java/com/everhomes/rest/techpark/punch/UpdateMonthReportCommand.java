// @formatter:off
package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型,填organization</li>
 * <li>ownerId: 公司id</li>
 * <li>monthReportId: 月报id</li>
 * </ul>
 */
public class UpdateMonthReportCommand {

	private String ownerType;

	private Long ownerId;

	private Long monthReportId;

	public UpdateMonthReportCommand() {

	}

	public UpdateMonthReportCommand(String ownerType, Long ownerId, Long monthReportId) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.monthReportId = monthReportId;
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

	public Long getMonthReportId() {
		return monthReportId;
	}

	public void setMonthReportId(Long monthReportId) {
		this.monthReportId = monthReportId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
