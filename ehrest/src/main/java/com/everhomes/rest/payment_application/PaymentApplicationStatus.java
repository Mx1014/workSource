package com.everhomes.rest.payment_application;

/**
 * <ul>
 *     <li>0: inactive; 1: waiting for approval; 2: QUALIFIED; 3: UNQUALIFIED</li>
 * </ul>
 * Created by ying.xiong on 2017/12/27.
 */
public enum PaymentApplicationStatus {
    INACTIVE((byte)0), WATING((byte)1), QUALIFIED((byte)2), UNQUALIFIED((byte)3);

    private Byte code;
    private PaymentApplicationStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static PaymentApplicationStatus fromCode(Byte code) {
        if(code != null) {
            PaymentApplicationStatus[] values = PaymentApplicationStatus.values();
            for(PaymentApplicationStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
