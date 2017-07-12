// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>warningDays: 提醒天数</li>
 * <li>warningHours: 提醒小时数</li>
 * <li>warningTime: 提醒时长</li>
 * <li>orderDays: 订单天数</li>
 * <li>orderHours: 订单小时数</li>
 * <li>orderTime: 订单时长</li>
 * </ul>
 */
public class ActivityTimeResponse {
	private Integer namespaceId;
	private Integer warningDays;
	private Integer warningHours;
	private Long warningTime;
	private Integer orderDays;
	private Integer orderHours;
	private Long orderTime;
	
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
