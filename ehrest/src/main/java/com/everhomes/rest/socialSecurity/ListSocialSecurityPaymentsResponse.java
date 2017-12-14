// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>socialSecurityPayments: 社保缴费列表 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDTO}</li>
 * <li>paymentMonth: 月份如201702</li>
 * </ul>
 */
public class ListSocialSecurityPaymentsResponse {

	private Long nextPageAnchor;

	@ItemType(SocialSecurityPaymentDTO.class)
	private List<SocialSecurityPaymentDTO> socialSecurityPayments;

	private String paymentMonth;

	public ListSocialSecurityPaymentsResponse() {

	}

	public ListSocialSecurityPaymentsResponse(Long nextPageAnchor, List<SocialSecurityPaymentDTO> socialSecurityPayments, String paymentMonth) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.socialSecurityPayments = socialSecurityPayments;
		this.paymentMonth = paymentMonth;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
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
