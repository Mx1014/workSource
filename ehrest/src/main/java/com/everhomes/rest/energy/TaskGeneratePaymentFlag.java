package com.everhomes.rest.energy;

/**
 * Created by ying.xiong on 2017/10/27.
 */
public enum TaskGeneratePaymentFlag {
    NON_GENERATE((byte) 0), GENERATED((byte) 1);

    private Byte code;

    TaskGeneratePaymentFlag(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static TaskGeneratePaymentFlag fromCode(Byte code) {
        for (TaskGeneratePaymentFlag type : TaskGeneratePaymentFlag.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
