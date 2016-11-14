// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>days: 天数</li>
 * <li>hours: 小时数</li>
 * </ul>
 */
public class ActivityWarningResponse {
	private Integer namespaceId;
	private Integer days;
	private Integer hours;
	private Long time;
	
	public ActivityWarningResponse() {
		super();
	}
	
	public ActivityWarningResponse(Integer namespaceId, Integer days, Integer hours, Long time) {
		super();
		this.namespaceId = namespaceId;
		this.days = days;
		this.hours = hours;
		this.time = time;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getHours() {
		return hours;
	}
	public void setHours(Integer hours) {
		this.hours = hours;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
