// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>detailId: detailid</li>
 * <li>userName: 用户姓名</li>
 * <li>socialSecurityNo: 社保/公积金号</li>
 * <li>payCurrentSocialSecurityFlag: 当月缴社保标识</li>
 * <li>afterPaySocialSecurityFlag: 补缴社保标识</li>
 * <li>payCurrentAccumulationFundFlag: 当月缴公积金标识</li>
 * <li>afterPayAccumulationFundFlag: 补缴公积金标识</li>
 * <li>paymentMonth: 月份如201702</li>
 * <li>socialSecurityPayments: 社保缴纳项列表 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * <li>afterSocialSecurityPayments: 社保补缴缴纳项列表 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * <li>accumulationFundPayments: 公积金缴纳项列表 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * <li>afterAccumulationFundPayments: 公积金补缴缴纳项列表 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * </ul>
 */
public class GetSocialSecurityPaymentDetailsResponse {

	private Long detailId;
	private String userName;
	private String socialSecurityNo;
	
	private Byte payCurrentSocialSecurityFlag;

	private Byte afterPaySocialSecurityFlag;

	private Byte payCurrentAccumulationFundFlag;

	private Byte afterPayAccumulationFundFlag;

	private String paymentMonth;
 
	private SocialSecurityPaymentDetailDTO socialSecurityPayments;
 
	private SocialSecurityPaymentDetailDTO afterSocialSecurityPayments;
 
	private SocialSecurityPaymentDetailDTO accumulationFundPayments;
 
	private SocialSecurityPaymentDetailDTO afterAccumulationFundPayments;
 
	public Byte getPayCurrentSocialSecurityFlag() {
		return payCurrentSocialSecurityFlag;
	}

	public void setPayCurrentSocialSecurityFlag(Byte payCurrentSocialSecurityFlag) {
		this.payCurrentSocialSecurityFlag = payCurrentSocialSecurityFlag;
	}

	public Byte getAfterPaySocialSecurityFlag() {
		return afterPaySocialSecurityFlag;
	}

	public void setAfterPaySocialSecurityFlag(Byte afterPaySocialSecurityFlag) {
		this.afterPaySocialSecurityFlag = afterPaySocialSecurityFlag;
	}

	public Byte getPayCurrentAccumulationFundFlag() {
		return payCurrentAccumulationFundFlag;
	}

	public void setPayCurrentAccumulationFundFlag(Byte payCurrentAccumulationFundFlag) {
		this.payCurrentAccumulationFundFlag = payCurrentAccumulationFundFlag;
	}

	public Byte getAfterPayAccumulationFundFlag() {
		return afterPayAccumulationFundFlag;
	}

	public void setAfterPayAccumulationFundFlag(Byte afterPayAccumulationFundFlag) {
		this.afterPayAccumulationFundFlag = afterPayAccumulationFundFlag;
	}

	public String getPaymentMonth() {
		return paymentMonth;
	}

	public void setPaymentMonth(String paymentMonth) {
		this.paymentMonth = paymentMonth;
	}
 

	public SocialSecurityPaymentDetailDTO getSocialSecurityPayments() {
		return socialSecurityPayments;
	}

	public void setSocialSecurityPayments(SocialSecurityPaymentDetailDTO socialSecurityPayments) {
		this.socialSecurityPayments = socialSecurityPayments;
	}

	public SocialSecurityPaymentDetailDTO getAfterSocialSecurityPayments() {
		return afterSocialSecurityPayments;
	}

	public void setAfterSocialSecurityPayments(
			SocialSecurityPaymentDetailDTO afterSocialSecurityPayments) {
		this.afterSocialSecurityPayments = afterSocialSecurityPayments;
	}

	public SocialSecurityPaymentDetailDTO getAccumulationFundPayments() {
		return accumulationFundPayments;
	}

	public void setAccumulationFundPayments(SocialSecurityPaymentDetailDTO accumulationFundPayments) {
		this.accumulationFundPayments = accumulationFundPayments;
	}

	public SocialSecurityPaymentDetailDTO getAfterAccumulationFundPayments() {
		return afterAccumulationFundPayments;
	}

	public void setAfterAccumulationFundPayments(
			SocialSecurityPaymentDetailDTO afterAccumulationFundPayments) {
		this.afterAccumulationFundPayments = afterAccumulationFundPayments;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSocialSecurityNo() {
		return socialSecurityNo;
	}

	public void setSocialSecurityNo(String socialSecurityNo) {
		this.socialSecurityNo = socialSecurityNo;
	}

}
