// @formatter:off
package com.everhomes.sms;

/**
 * <ul>
 *     <li>SYSTEM((byte) 0): 系统</li>
 *     <li>MANUAL((byte) 1): 手动</li>
 * </ul>
 */
public enum SmsBlackListCreateType {
    SYSTEM((byte) 0), MANUAL((byte) 1);

    private Byte code;

    SmsBlackListCreateType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static SmsBlackListCreateType fromCode(Byte code) {
        if (code != null) {
            for (SmsBlackListCreateType type : SmsBlackListCreateType.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
