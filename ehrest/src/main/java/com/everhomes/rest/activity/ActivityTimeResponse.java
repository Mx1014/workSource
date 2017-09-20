// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>categoryId: categoryId</li>
 *     <li>warningDays: 提醒天数</li>
 *     <li>warningHours: 提醒小时数</li>
 *     <li>warningTime: 提醒时长</li>
 *     <li>orderDays: 订单天数</li>
 *     <li>orderHours: 订单小时数</li>
 *     <li>orderTime: 订单时长</li>
 *     <li>wechatSignup: 是否支持微信报名，0-不支持，1-支持 参考  参考{@link com.everhomes.rest.activity.WechatSignupFlag }</li>
 * </ul>
 */
public class ActivityTimeResponse {
	private Integer namespaceId;
	private Long categoryId;
	private Integer warningDays;
	private Integer warningHours;
	private Long warningTime;
	private Integer orderDays;
	private Integer orderHours;
	private Long orderTime;
	private Byte wechatSignup;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Integer getWarningDays() {
		return warningDays;
	}

	public void setWarningDays(Integer warningDays) {
		this.warningDays = warningDays;
	}

	public Integer getWarningHours() {
		return warningHours;
	}

	public void setWarningHours(Integer warningHours) {
		this.warningHours = warningHours;
	}

	public Long getWarningTime() {
		return warningTime;
	}

	public void setWarningTime(Long warningTime) {
		this.warningTime = warningTime;
	}

	public Integer getOrderDays() {
		return orderDays;
	}

	public void setOrderDays(Integer orderDays) {
		this.orderDays = orderDays;
	}

	public Integer getOrderHours() {
		return orderHours;
	}

	public void setOrderHours(Integer orderHours) {
		this.orderHours = orderHours;
	}

	public Long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Long orderTime) {
		this.orderTime = orderTime;
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
