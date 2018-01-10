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
 * <li>socialSecurityPayments: 社保缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * <li>accumulationFundPayments: 公积金缴纳项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityPaymentDetailDTO}</li>
 * </ul>
 */
public class IncreseSocialSecurityCommand {

	private String ownerType;

	private Long ownerId;

	@ItemType(Long.class)
	private List<Long> detailIds;

	private SocialSecurityPaymentDetailDTO  socialSecurityPayments;

	private SocialSecurityPaymentDetailDTO accumulationFundPayments;

	public IncreseSocialSecurityCommand() {

	}

	public IncreseSocialSecurityCommand(String ownerType, Long ownerId, List<Long> detailIds, SocialSecurityPaymentDetailDTO  socialSecurityPayments, SocialSecurityPaymentDetailDTO accumulationFundPayments) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.detailIds = detailIds;
		this.socialSecurityPayments = socialSecurityPayments;
		this.accumulationFundPayments = accumulationFundPayments;
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

	public SocialSecurityPaymentDetailDTO  getSocialSecurityPayments() {
		return socialSecurityPayments;
	}

	public void setSocialSecurityPayments(SocialSecurityPaymentDetailDTO  socialSecurityPayments) {
		this.socialSecurityPayments = socialSecurityPayments;
	}

	public SocialSecurityPaymentDetailDTO getAccumulationFundPayments() {
		return accumulationFundPayments;
	}

	public void setAccumulationFundPayments(SocialSecurityPaymentDetailDTO accumulationFundPayments) {
		this.accumulationFundPayments = accumulationFundPayments;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
