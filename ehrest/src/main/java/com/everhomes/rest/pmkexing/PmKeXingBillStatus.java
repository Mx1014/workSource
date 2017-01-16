package com.everhomes.rest.pmkexing;

/**
 * <ul>
 *     <li>UNPAID(0): 未缴纳</li>
 *     <li>PAID(1): 已缴纳</li>
 * </ul>
 */
public enum PmKeXingBillStatus {

    UNPAID((byte) 0), PAID((byte) 1);

    private Byte code;

    PmKeXingBillStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static PmKeXingBillStatus fromCode(Byte code) {
        for (PmKeXingBillStatus type : PmKeXingBillStatus.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
