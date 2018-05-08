// @formatter:off
package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id</li>
 * <li>checkInStartDay: 入职开始时间</li>
 * <li>checkInEndDay: 入职结束时间</li>
 * <li>departmentId: 部门id</li>
 * <li>keyWords: 员工姓名</li>
 * </ul>
 */
public class ExportVacationBalancesCommand {

	private Long organizationId;

	private Long checkInStartDay;

	private Long checkInEndDay;

	private Long departmentId;

	private String keyWords;

	public ExportVacationBalancesCommand() {

	}

	public ExportVacationBalancesCommand(Long organizationId, Long checkInStartDay, Long checkInEndDay, Long departmentId, String keyWords) {
		super();
		this.organizationId = organizationId;
		this.checkInStartDay = checkInStartDay;
		this.checkInEndDay = checkInEndDay;
		this.departmentId = departmentId;
		this.keyWords = keyWords;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getCheckInStartDay() {
		return checkInStartDay;
	}

	public void setCheckInStartDay(Long checkInStartDay) {
		this.checkInStartDay = checkInStartDay;
	}

	public Long getCheckInEndDay() {
		return checkInEndDay;
	}

	public void setCheckInEndDay(Long checkInEndDay) {
		this.checkInEndDay = checkInEndDay;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
