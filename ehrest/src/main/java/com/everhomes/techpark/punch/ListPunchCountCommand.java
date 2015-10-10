// @formatter:off
package com.everhomes.techpark.punch;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>companyId：公司id</li>
 * <li>startDay: 开始时间</li>
 * <li>endDay：结束时间</li>
 * </ul>
 */
public class ListPunchCountCommand {
	@NotNull
	private Long    companyId;
	
	private String startDay;
	
	private String endDay;
	
	
	public ListPunchCountCommand() {
    }
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
