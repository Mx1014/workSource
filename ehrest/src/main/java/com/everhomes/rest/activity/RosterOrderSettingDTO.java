// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

import java.io.PrintWriter;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>categoryId: categoryId</li>
 *     <li>days: 天数</li>
 *     <li>hours: 小时数</li>
 *     <li>time: 转换后的time</li>
 *     <li>wechatSignup: 是否支持微信报名，0-不支持，1-支持 参考  参考{@link com.everhomes.rest.activity.WechatSignupFlag }</li>
 * </ul>
 */
public class RosterOrderSettingDTO {
	private Integer namespaceId;
	private Long categoryId;
	private Integer days;
	private Integer hours;
	private Long time;
	private Byte wechatSignup;

	public RosterOrderSettingDTO() {
		super();
	}

	public RosterOrderSettingDTO(Integer namespaceId, Integer days, Integer hours, Long time) {
		super();
		this.namespaceId = namespaceId;
		this.days = days;
		this.hours = hours;
		this.time = time;
	}

	public RosterOrderSettingDTO(Integer namespaceId, Long categoryId, Integer days, Integer hours, Long time, Byte wechatSignup) {
		super();
		this.namespaceId = namespaceId;
		this.days = days;
		this.hours = hours;
		this.time = time;
		this.wechatSignup = wechatSignup;
		this.categoryId = categoryId;
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

	public Byte getWechatSignup() {
		return wechatSignup;
	}

	public void setWechatSignup(Byte wechatSignup) {
		this.wechatSignup = wechatSignup;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
