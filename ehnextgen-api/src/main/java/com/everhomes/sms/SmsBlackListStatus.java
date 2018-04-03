// @formatter:off
package com.everhomes.sms;

/**
 * <ul>
 *     <li>PASS((byte)0): 通过</li>
 *     <li>BLOCK((byte)1): 不通过</li>
 * </ul>
 */
public enum SmsBlackListStatus {
    PASS((byte) 0), BLOCK((byte) 1);

    private Byte code;

    SmsBlackListStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static SmsBlackListStatus fromCode(Byte code) {
        if (code != null) {
            for (SmsBlackListStatus status : SmsBlackListStatus.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
