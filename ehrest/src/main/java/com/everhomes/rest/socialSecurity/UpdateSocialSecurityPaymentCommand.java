// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型 organization</li>
 * <li>ownerId: 所属id 公司id</li>
 * <li>detailId: 人员的detailId)</li>
 * <li>payCurrentSocialSecurityFlag(Byte|当月缴社保标识</li>
 * <li>afterPaySocialSecurityFlag: 补缴社保标识</li>
 * <li>payCurrentAccumulationFundFlag: 当月缴公积金标识</li>
 * <li>afterPayAccumulationFundFlag: 补缴公积金标识</li>
 * <li>paymentMonth: 月份如201702</li>
 * <li>householdType: 户籍类型</li>
 * <li>socialSecurityPayment: 社保缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * <li>afterSocialSecurityPayment: 社保补缴缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * <li>accumulationFundPayment: 公积金缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * <li>afterAccumulationFundPayment: 公积金补缴缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * </ul>
 */
public class UpdateSocialSecurityPaymentCommand {

	private String ownerType;

	private Long ownerId;

	private Long detailId;

	private String householdType;

	private Byte payCurrentSocialSecurityFlag;

	private Byte afterPaySocialSecurityFlag;

	private Byte payCurrentAccumulationFundFlag;

	private Byte afterPayAccumulationFundFlag;

	private String paymentMonth;

	private SocialSecurityPaymentDetailDTO socialSecurityPayment;

	private SocialSecurityPaymentDetailDTO afterSocialSecurityPayment;

	private SocialSecurityPaymentDetailDTO accumulationFundPayment;

	private SocialSecurityPaymentDetailDTO afterAccumulationFundPayment;


	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
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

	public SocialSecurityPaymentDetailDTO getAccumulationFundPayment() {
		return accumulationFundPayment;
	}

	public void setAccumulationFundPayment(SocialSecurityPaymentDetailDTO accumulationFundPayment) {
		this.accumulationFundPayment = accumulationFundPayment;
	}

	public SocialSecurityPaymentDetailDTO getAfterAccumulationFundPayment() {
		return afterAccumulationFundPayment;
	}

	public void setAfterAccumulationFundPayment(SocialSecurityPaymentDetailDTO afterAccumulationFundPayment) {
		this.afterAccumulationFundPayment = afterAccumulationFundPayment;
	}

	public SocialSecurityPaymentDetailDTO getAfterSocialSecurityPayment() {
		return afterSocialSecurityPayment;
	}

	public void setAfterSocialSecurityPayment(SocialSecurityPaymentDetailDTO afterSocialSecurityPayment) {
		this.afterSocialSecurityPayment = afterSocialSecurityPayment;
	}

	public SocialSecurityPaymentDetailDTO getSocialSecurityPayment() {
		return socialSecurityPayment;
	}

	public void setSocialSecurityPayment(SocialSecurityPaymentDetailDTO socialSecurityPayment) {
		this.socialSecurityPayment = socialSecurityPayment;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getHouseholdType() {
		return householdType;
	}

	public void setHouseholdType(String householdType) {
		this.householdType = householdType;
	}

	public Byte getPayCurrentSocialSecurityFlag() {
		return payCurrentSocialSecurityFlag;
	}

	public void setPayCurrentSocialSecurityFlag(Byte payCurrentSocialSecurityFlag) {
		this.payCurrentSocialSecurityFlag = payCurrentSocialSecurityFlag;
	}
}
