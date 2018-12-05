// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>paymentAuthOperateLogs: 授权日志列表{@link com.everhomes.rest.enterprisepaymentauth.PaymentAuthOperateLogDTO}</li>
 * <li>nextPageOffset: 下页</li>
 * </ul>
 */
public class ListEnterprisePaymentAuthOperateLogsResponse {

	@ItemType(PaymentAuthOperateLogDTO.class)
	private List<PaymentAuthOperateLogDTO> paymentAuthOperateLogs;
	private Integer nextPageOffset;

	public ListEnterprisePaymentAuthOperateLogsResponse() {

	}


	public List<PaymentAuthOperateLogDTO> getPaymentAuthOperateLogs() {
		return paymentAuthOperateLogs;
	}

	public void setPaymentAuthOperateLogs(List<PaymentAuthOperateLogDTO> paymentAuthOperateLogs) {
		this.paymentAuthOperateLogs = paymentAuthOperateLogs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
}
