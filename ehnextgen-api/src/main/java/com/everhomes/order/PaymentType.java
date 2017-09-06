//@formatter:off
package com.everhomes.order;

/**
 * Created by Wentian Wang on 2017/9/6.
 */

public enum PaymentType {
    WEICHAT_APP(1),APLIPAY(8);
    private Integer code;

    PaymentType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static PaymentType fromCode(Integer code) {
        for (PaymentType type : PaymentType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

}
