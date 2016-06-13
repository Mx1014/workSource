// @formatter:off
package com.everhomes.rest.techpark.punch;

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
	private Long    enterpriseId;
	
	private String startDay;
	
	private String endDay;
	
	private Long enterpriseGroupId;
	
	public ListPunchCountCommand() {
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
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public Long getEnterpriseGroupId() {
		return enterpriseGroupId;
	}
	public void setEnterpriseGroupId(Long enterpriseGroupId) {
		this.enterpriseGroupId = enterpriseGroupId;
	}
}
