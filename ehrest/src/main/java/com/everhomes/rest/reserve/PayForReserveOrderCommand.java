package com.everhomes.rest.reserve;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 订单id</li>
 * </ul>
 */
public class PayForReserveOrderCommand {
    private Long id;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
