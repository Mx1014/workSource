package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/11/16.
 */
public enum YearQuarter {
    THE_FIRST_QUARTER(1), THE_SECOND_QUARTER(2), THE_THIRD_QUARTER(3), THE_FOURTH_QUARTER(4);

    private int code;

    private YearQuarter(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CustomerType fromStatus(int code) {
        for(CustomerType v : CustomerType.values()) {
            if(v.getCode() == code)
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
