// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>exchangeId: exchangeId</li>
 * </ul>
 */
public class UpdateFlowButtonOrderCommand {

    @NotNull private Long id;
    @NotNull private Long exchangeId;

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
