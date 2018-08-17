//@formatter:off
package com.everhomes.rest.asset;

/**
 * @author created by yangcx
 * @date 2018年5月23日----下午5:56:48
 */
/**
 *<ul>
 * <li>payState: 支付状态，1：支付成功，0：支付失败</li>
 *</ul>
 */
public class GetPayBillsForEntResultResp {
	private Integer payState;

	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}
}
