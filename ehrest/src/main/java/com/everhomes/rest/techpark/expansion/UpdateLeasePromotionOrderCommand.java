package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 资源id</li>
 * <li>exchangeId: 交换顺序id</li>
 * </ul>
 */
public class UpdateLeasePromotionOrderCommand {
    private Long id;
    private Long exchangeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
