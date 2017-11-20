package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ownerType: 帐户类型（如EhUsers、EhOrganizations），{@link com.everhomes.rest.order.OwnerType}</li>
 *     <li>ownerId: 帐户对应的ID（如用户ID、企业ID）</li>
 * </ul>
 */
public class GetPaymentBalanceCommand {
	private String ownerType;
	
	private Long ownerId;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
