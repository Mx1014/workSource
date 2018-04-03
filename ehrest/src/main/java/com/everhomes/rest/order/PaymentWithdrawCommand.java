package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ownerType: 帐号类型，如EhOrganizations, EhUsers，{@link com.everhomes.rest.order.OwnerType}</li>
 *     <li>ownerId: 帐号ID， 如企业ID、用户ID</li>
 *     <li>amount: 提现金额，单位分</li>
 * </ul>
 */
public class PaymentWithdrawCommand {
    private String ownerType;
    
    private Long ownerId;
    
    private Long amount;

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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
