//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class CheckPaymentUserCommand {
    private Long userId;
    private String ownerType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
}
