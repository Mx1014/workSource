// @formatter:off
package com.everhomes.rest.activity;


/**
 * <ul>
 * 	<li>NULL: 0, 未设置付款方</li>
 *  <li>UNDER_REVIEW: 1，付款方账号审核中</li>
 *  <li>IN_USE: 2，付款方账号可以使用</li>
 * </ul>
 */
public enum ActivityPayeeStatusType {
    NULL((byte) 0), UNDER_REVIEW((byte) 1), IN_USE((byte)2);
    private Byte code;

    ActivityPayeeStatusType(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ActivityPayeeStatusType fromStringCode(String code) {
        for (ActivityPayeeStatusType flag : ActivityPayeeStatusType.values()) {
            if (flag.name().equalsIgnoreCase(code)) {
                return flag;
            }
        }
        return NULL;
    }

    public static ActivityPayeeStatusType fromCode(Byte code) {
        if(null == code){
            return null;
        }
        for (ActivityPayeeStatusType flag : ActivityPayeeStatusType.values()) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
