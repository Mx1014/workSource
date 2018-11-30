// @formatter:off
package com.everhomes.rest.officecubicle;


import java.util.List;

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
    private Integer namespaceId;
    private String ownerType;
    private Long communityId;
    private Long spaceId;
    private Byte refundStrategy;
    private String refundTip;
    private List<OfficeCubicleRefundRuleDTO> refundStrategies;
    
    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

    public Byte getRefundStrategy() {
		return refundStrategy;
	}

	public void setRefundStrategy(Byte refundStrategy) {
		this.refundStrategy = refundStrategy;
	}

	public String getRefundTip() {
		return refundTip;
	}

	public void setRefundTip(String refundTip) {
		this.refundTip = refundTip;
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
