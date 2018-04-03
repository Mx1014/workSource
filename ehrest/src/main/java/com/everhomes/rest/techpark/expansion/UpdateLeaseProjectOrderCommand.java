package com.everhomes.rest.techpark.expansion;

/**
 * @author sw on 2017/10/17.
 */
public class UpdateLeaseProjectOrderCommand {
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
}
