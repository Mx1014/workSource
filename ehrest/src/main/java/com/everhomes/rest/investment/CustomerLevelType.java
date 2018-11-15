package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

public enum CustomerLevelType {
    FIRST_CUSTOMER(3L),POTENTIAL_CUSTOMER(4L),INTENTIONAL_CUSTOMER(5L),
    REGISTERED_CUSTOMER(6L), HISTORY_CUSTOMER(7L), LOSS_CUSTOMER(5003L);

    private Long code;

    private CustomerLevelType(Long code) {
        this.code = code;
    }

    public Long getCode() {
        return code;
    }

    public static CustomerLevelType fromCode(Long code) {
        for (CustomerLevelType v : CustomerLevelType.values()) {
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
