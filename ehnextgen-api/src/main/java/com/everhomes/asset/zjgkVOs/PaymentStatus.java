package com.everhomes.asset.zjgkVOs;

/**
 * Created by Wentian on 2017/9/10.
 */
public enum PaymentStatus {
    IN_PROCESS("审核中"),PAID_OFF("已缴"),UN_PAID("未缴"),SUSPEND("待处理");
    private String code;
    private PaymentStatus(String code){
        this.code = code;
    }
    public String getCode() {
        return this.code;
    }

    public static PaymentStatus fromCode(String code) {
        if(code != null) {
            PaymentStatus[] values = PaymentStatus.values();
            for(PaymentStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
