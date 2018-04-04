// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageOffset: 下页页码</li>
 * <li>socialSecurityPayments: 社保缴费列表 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDTO}</li>
 * <li>paymentMonth: 月份如201702</li>
 * </ul>
 */
public class ListSocialSecurityPaymentsResponse {

	private Integer nextPageOffset;

	@ItemType(SocialSecurityPaymentDTO.class)
	private List<SocialSecurityPaymentDTO> socialSecurityPayments;

	private String paymentMonth;

	public ListSocialSecurityPaymentsResponse() {

	}

	public ListSocialSecurityPaymentsResponse(Integer nextPageOffset, List<SocialSecurityPaymentDTO> socialSecurityPayments, String paymentMonth) {
		this.nextPageOffset = nextPageOffset;
		this.socialSecurityPayments = socialSecurityPayments;
		this.paymentMonth = paymentMonth;
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<SocialSecurityPaymentDTO> getSocialSecurityPayments() {
		return socialSecurityPayments;
	}

	public void setSocialSecurityPayments(List<SocialSecurityPaymentDTO> socialSecurityPayments) {
		this.socialSecurityPayments = socialSecurityPayments;
	}

	public String getPaymentMonth() {
		return paymentMonth;
	}

	public void setPaymentMonth(String paymentMonth) {
		this.paymentMonth = paymentMonth;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
