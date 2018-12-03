// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>paymentEmployeeChangeLogs: 记录列表 {@link com.everhomes.rest.enterprisepaymentauth.PaymentAuthOperateLogDTO}</li>
 * <li>nextPageOffset: 下页</li>
 * </ul>
 */
public class ListEnterpriseEmployeePaymentLimitChangeLogsResponse {
	private List<PaymentAuthOperateLogDTO> paymentEmployeeChangeLogs;
	private Integer nextPageOffset;

	public List<PaymentAuthOperateLogDTO> getPaymentEmployeeChangeLogs() {
		return paymentEmployeeChangeLogs;
	}

	public void setPaymentEmployeeChangeLogs(List<PaymentAuthOperateLogDTO> paymentEmployeeChangeLogs) {
		this.paymentEmployeeChangeLogs = paymentEmployeeChangeLogs;
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
