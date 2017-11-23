//@formatter:off
package com.everhomes.rest.order;

/**
 * Created by Wentian Wang on 2017/9/6.
 */

public enum OrderPaymentStatus {
    SUCCESS(1),FAILED(2),CANCELED(3);
    private Integer code;

    OrderPaymentStatus(Integer code){
        this.code = code;
    }
    public static OrderPaymentStatus fromCode(Integer code) {
        for (OrderPaymentStatus status : OrderPaymentStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
    public Integer getCode(){return code;}
}
