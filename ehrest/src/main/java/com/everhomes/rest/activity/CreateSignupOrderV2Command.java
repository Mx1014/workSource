// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>activityId: 活动ID</li>
 *     <li>clientAppName: Realm值，app客户端必传</li>
 *     <li>paymentType: 支付方式，微信公众号支付方式必填，9-公众号支付 参考{@link com.everhomes.order.PaymentType}</li>
 * </ul>
 */
public class CreateSignupOrderV2Command {
	@NotNull
	private Long activityId;

	private String clientAppName;

	private Integer paymentType;

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getClientAppName() {
		return clientAppName;
	}

	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
