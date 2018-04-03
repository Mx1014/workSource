package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>unjoinPunchGroupCount: 未设置考勤的人数</li>
 * <li>allEmployeeCount: 总人数</li>
 * <li>unSchedulingCount: 未排班人数</li>
 * </ul>
 */
public class GetPunchGroupsCountResponse {

	private Integer unjoinPunchGroupCount;

	private Integer allEmployeeCount;

	private Integer unSchedulingCount;


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Integer getAllEmployeeCount() {
		return allEmployeeCount;
	}

	public void setAllEmployeeCount(Integer allEmployeeCount) {
		this.allEmployeeCount = allEmployeeCount;
	}

	public Integer getUnjoinPunchGroupCount() {
		return unjoinPunchGroupCount;
	}

	public void setUnjoinPunchGroupCount(Integer unjoinPunchGroupCount) {
		this.unjoinPunchGroupCount = unjoinPunchGroupCount;
	}

	public Integer getUnSchedulingCount() {
		return unSchedulingCount;
	}

	public void setUnSchedulingCount(Integer unSchedulingCount) {
		this.unSchedulingCount = unSchedulingCount;
	}
}
