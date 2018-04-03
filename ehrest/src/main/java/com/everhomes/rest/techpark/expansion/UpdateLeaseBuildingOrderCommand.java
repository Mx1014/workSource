package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/4.
 */
public class UpdateLeaseBuildingOrderCommand {
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
