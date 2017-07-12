package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：订单id</li>
 * </ul>
 */
public class GetRentalBillPayInfoCommand {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
