package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

public enum CustomerStatisticType {
    STATISTIC_DAILY(1),STATISTIC_MONTHLY(2),STATISTIC_ALL(3);
    //REGISTERED_CUSTOMER(4), HISTORY_CUSTOMER(5), LOSS_CUSTOMER();

    private Integer code;

    private CustomerStatisticType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static CustomerStatisticType fromCode(Integer code) {
        for (CustomerStatisticType v : CustomerStatisticType.values()) {
            if (v.getCode().equals(code))
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
