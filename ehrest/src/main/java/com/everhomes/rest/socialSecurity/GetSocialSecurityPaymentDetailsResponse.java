// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>detailId: detailid</li>
 * <li>userName: 用户姓名</li>
 * <li>isWork: 员工在职状态{@link IsWork}</li>
 * <li>socialSecurityNo: 社保/公积金号</li>
 * <li>payCurrentSocialSecurityFlag: 当月缴社保标识</li>
 * <li>afterPaySocialSecurityFlag: 补缴社保标识</li>
 * <li>payCurrentAccumulationFundFlag: 当月缴公积金标识</li>
 * <li>afterPayAccumulationFundFlag: 补缴公积金标识</li>
 * <li>paymentMonth: 月份如201702</li>
 * <li>socialSecurityPayment: 社保缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * <li>afterSocialSecurityPayment: 社保补缴缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * <li>accumulationFundPayment: 公积金缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * <li>afterAccumulationFundPayment: 公积金补缴缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * </ul>
 */
public class GetSocialSecurityPaymentDetailsResponse {

	private Long detailId;
	private String userName;
	private String socialSecurityNo;
	private String idNumber;
	private BigDecimal salaryRadix;
	private Byte isWork;

	private Byte payCurrentSocialSecurityFlag;

	private Byte afterPaySocialSecurityFlag;

	private Byte payCurrentAccumulationFundFlag;

	private Byte afterPayAccumulationFundFlag;

	private String paymentMonth;
 
	private SocialSecurityPaymentDetailDTO socialSecurityPayment;
 
	private SocialSecurityPaymentDetailDTO afterSocialSecurityPayment;
 
	private SocialSecurityPaymentDetailDTO accumulationFundPayment;
 
	private SocialSecurityPaymentDetailDTO afterAccumulationFundPayment;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public Byte getAfterPayAccumulationFundFlag() {
		return afterPayAccumulationFundFlag;
	}

	public void setAfterPayAccumulationFundFlag(Byte afterPayAccumulationFundFlag) {
		this.afterPayAccumulationFundFlag = afterPayAccumulationFundFlag;
	}

	public Byte getAfterPaySocialSecurityFlag() {
		return afterPaySocialSecurityFlag;
	}

	public void setAfterPaySocialSecurityFlag(Byte afterPaySocialSecurityFlag) {
		this.afterPaySocialSecurityFlag = afterPaySocialSecurityFlag;
	}

	public SocialSecurityPaymentDetailDTO getAfterSocialSecurityPayment() {
		return afterSocialSecurityPayment;
	}

	public void setAfterSocialSecurityPayment(SocialSecurityPaymentDetailDTO afterSocialSecurityPayment) {
		this.afterSocialSecurityPayment = afterSocialSecurityPayment;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Byte getPayCurrentAccumulationFundFlag() {
		return payCurrentAccumulationFundFlag;
	}

	public void setPayCurrentAccumulationFundFlag(Byte payCurrentAccumulationFundFlag) {
		this.payCurrentAccumulationFundFlag = payCurrentAccumulationFundFlag;
	}

	public Byte getPayCurrentSocialSecurityFlag() {
		return payCurrentSocialSecurityFlag;
	}

	public void setPayCurrentSocialSecurityFlag(Byte payCurrentSocialSecurityFlag) {
		this.payCurrentSocialSecurityFlag = payCurrentSocialSecurityFlag;
	}

	public String getPaymentMonth() {
		return paymentMonth;
	}

	public void setPaymentMonth(String paymentMonth) {
		this.paymentMonth = paymentMonth;
	}

	public String getSocialSecurityNo() {
		return socialSecurityNo;
	}

	public void setSocialSecurityNo(String socialSecurityNo) {
		this.socialSecurityNo = socialSecurityNo;
	}

	public SocialSecurityPaymentDetailDTO getSocialSecurityPayment() {
		return socialSecurityPayment;
	}

	public void setSocialSecurityPayment(SocialSecurityPaymentDetailDTO socialSecurityPayment) {
		this.socialSecurityPayment = socialSecurityPayment;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public BigDecimal getSalaryRadix() {
		return salaryRadix;
	}

	public void setSalaryRadix(BigDecimal salaryRadix) {
		this.salaryRadix = salaryRadix;
	}

	public Byte getIsWork() {
		return isWork;
	}

	public void setIsWork(Byte isWork) {
		this.isWork = isWork;
	}
}
