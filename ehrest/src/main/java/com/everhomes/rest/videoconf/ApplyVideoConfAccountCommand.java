package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterpriseId：企业id</li>
 * <li>quantity: 申请数量</li>
 * </ul>
 */
public class ApplyVideoConfAccountCommand {

	private Long enterpriseId;
	
	private Integer quantity;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
