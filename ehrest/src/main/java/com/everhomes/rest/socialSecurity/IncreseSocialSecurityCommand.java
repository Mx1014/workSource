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
 * <li>detailIds: 人员的detailIds</li>
 * <li>socialSecurityPayment: 社保缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * <li>accumulationFundPayment: 公积金缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * </ul>
 */
public class IncreseSocialSecurityCommand {

	private String ownerType;

	private Long ownerId;

	@ItemType(Long.class)
	private List<Long> detailIds;

	private SocialSecurityPaymentDetailDTO  socialSecurityPayment;

	private SocialSecurityPaymentDetailDTO accumulationFundPayment;

	public IncreseSocialSecurityCommand() {

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

	public List<Long> getDetailIds() {
		return detailIds;
	}

	public void setDetailIds(List<Long> detailIds) {
		this.detailIds = detailIds;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public SocialSecurityPaymentDetailDTO getSocialSecurityPayment() {
		return socialSecurityPayment;
	}

	public void setSocialSecurityPayment(SocialSecurityPaymentDetailDTO socialSecurityPayment) {
		this.socialSecurityPayment = socialSecurityPayment;
	}

	public SocialSecurityPaymentDetailDTO getAccumulationFundPayment() {
		return accumulationFundPayment;
	}

	public void setAccumulationFundPayment(SocialSecurityPaymentDetailDTO accumulationFundPayment) {
		this.accumulationFundPayment = accumulationFundPayment;
	}
}
