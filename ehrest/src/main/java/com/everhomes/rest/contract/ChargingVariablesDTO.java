package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.asset.PaymentVariable;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/31.
 */
public class ChargingVariablesDTO {
    @ItemType(PaymentVariable.class)
    private List<PaymentVariable> chargingVariables;

    public List<PaymentVariable> getChargingVariables() {
        return chargingVariables;
    }

    public void setChargingVariables(List<PaymentVariable> chargingVariables) {
        this.chargingVariables = chargingVariables;
    }
}
