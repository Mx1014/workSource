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
 * <li>detailId: 人员的detailId)payCurrentSocialSecurityFlag(Byte|当月缴社保标识</li>
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
public class UpdateSocialSecurityPaymentCommand {

	private String ownerType;

	private Long ownerId;

	private Long detailId;

	private Byte afterPaySocialSecurityFlag;

	private Byte payCurrentAccumulationFundFlag;

	private Byte afterPayAccumulationFundFlag;

	private String paymentMonth;

	@ItemType(SocialSecurityPaymentDetailDTO.class)
	private List<SocialSecurityPaymentDetailDTO> socialSecurityPayments;

	@ItemType(SocialSecurityPaymentDetailDTO.class)
	private List<SocialSecurityPaymentDetailDTO> afterSocialSecurityPayments;

	@ItemType(SocialSecurityPaymentDetailDTO.class)
	private List<SocialSecurityPaymentDetailDTO> accumulationFundPayments;

	@ItemType(SocialSecurityPaymentDetailDTO.class)
	private List<SocialSecurityPaymentDetailDTO> afterAccumulationFundPayments;

	public UpdateSocialSecurityPaymentCommand() {

	}

	public UpdateSocialSecurityPaymentCommand(String ownerType, Long ownerId, Long detailId, Byte afterPaySocialSecurityFlag, Byte payCurrentAccumulationFundFlag, Byte afterPayAccumulationFundFlag, String paymentMonth, List<SocialSecurityPaymentDetailDTO> socialSecurityPayments, List<SocialSecurityPaymentDetailDTO> afterSocialSecurityPayments, List<SocialSecurityPaymentDetailDTO> accumulationFundPayments, List<SocialSecurityPaymentDetailDTO> afterAccumulationFundPayments) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.detailId = detailId;
		this.afterPaySocialSecurityFlag = afterPaySocialSecurityFlag;
		this.payCurrentAccumulationFundFlag = payCurrentAccumulationFundFlag;
		this.afterPayAccumulationFundFlag = afterPayAccumulationFundFlag;
		this.paymentMonth = paymentMonth;
		this.socialSecurityPayments = socialSecurityPayments;
		this.afterSocialSecurityPayments = afterSocialSecurityPayments;
		this.accumulationFundPayments = accumulationFundPayments;
		this.afterAccumulationFundPayments = afterAccumulationFundPayments;
	}

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

	public List<SocialSecurityPaymentDetailDTO> getSocialSecurityPayments() {
		return socialSecurityPayments;
	}

	public void setSocialSecurityPayments(List<SocialSecurityPaymentDetailDTO> socialSecurityPayments) {
		this.socialSecurityPayments = socialSecurityPayments;
	}

	public List<SocialSecurityPaymentDetailDTO> getAfterSocialSecurityPayments() {
		return afterSocialSecurityPayments;
	}

	public void setAfterSocialSecurityPayments(List<SocialSecurityPaymentDetailDTO> afterSocialSecurityPayments) {
		this.afterSocialSecurityPayments = afterSocialSecurityPayments;
	}

	public List<SocialSecurityPaymentDetailDTO> getAccumulationFundPayments() {
		return accumulationFundPayments;
	}

	public void setAccumulationFundPayments(List<SocialSecurityPaymentDetailDTO> accumulationFundPayments) {
		this.accumulationFundPayments = accumulationFundPayments;
	}

	public List<SocialSecurityPaymentDetailDTO> getAfterAccumulationFundPayments() {
		return afterAccumulationFundPayments;
	}

	public void setAfterAccumulationFundPayments(List<SocialSecurityPaymentDetailDTO> afterAccumulationFundPayments) {
		this.afterAccumulationFundPayments = afterAccumulationFundPayments;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
