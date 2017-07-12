package com.everhomes.rest.pmkexing;

/**
 * <ul>
 *     <li>UNPAID(0): 未缴纳</li>
 *     <li>PAID(1): 已缴纳</li>
 * </ul>
 */
public enum PmKeXingBillStatus {

    UNPAID((byte) 0, "未缴"), PAID((byte) 1, "已缴");

    private Byte code;
    private String name;

    PmKeXingBillStatus(Byte code, String name){
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
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

    public static PmKeXingBillStatus fromName(String name) {
        for (PmKeXingBillStatus type : PmKeXingBillStatus.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
