// @formatter:off
package com.everhomes.rest.officecubicle;


import java.util.List;

import com.everhomes.rest.officecubicle.admin.OfficeCubicleRefundTipDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 归属的类型</li>
 * <li>communityId: 归属的ID，如小区ID</li>
 * <li>spaceId: 空间ID</li>
 * <li>refundStrategy: 退款策略{@link com.everhomes.rest.rentalv2.admin.RentalOrderStrategy}</li>
 * <li>refundStrategies:退款策略{@link com.everhomes.rest.officecubicle.OfficeCubicleRefundRuleDTO}</li>
 * <li>refundTip:退款提示</li>
 * </ul>
 */
public class GetOfficeCubicleRefundRuleResponse {
    private Byte refundStrategy;
    private List<OfficeCubicleRefundTipDTO> refundTips;
    private List<OfficeCubicleRefundRuleDTO> refundStrategies;
    private Integer refundPrice;
    

    public Integer getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Byte getRefundStrategy() {
		return refundStrategy;
	}

	public void setRefundStrategy(Byte refundStrategy) {
		this.refundStrategy = refundStrategy;
	}

	public List<OfficeCubicleRefundTipDTO> getRefundTips() {
		return refundTips;
	}

	public void setRefundTips(List<OfficeCubicleRefundTipDTO> refundTips) {
		this.refundTips = refundTips;
	}

	public List<OfficeCubicleRefundRuleDTO> getRefundStrategies() {
		return refundStrategies;
	}

	public void setRefundStrategies(List<OfficeCubicleRefundRuleDTO> refundStrategies) {
		this.refundStrategies = refundStrategies;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
