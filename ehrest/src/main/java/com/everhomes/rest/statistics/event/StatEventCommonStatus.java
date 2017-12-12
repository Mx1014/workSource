package com.everhomes.rest.statistics.event;

/**
 * <ul>
 *     <li>INACTIVE((byte)0): INACTIVE</li>
 *     <li>WAITING_FOR_CONFIRMATION((byte)1): WAITING_FOR_CONFIRMATION</li>
 *     <li>ACTIVE((byte)2): ACTIVE</li>
 * </ul>
 */
public enum StatEventCommonStatus {

    INACTIVE((byte) 0), WAITING_FOR_CONFIRMATION((byte) 1), ACTIVE((byte) 2);

    private byte code;

    StatEventCommonStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static StatEventCommonStatus fromCode(Byte code) {
        if (code != null) {
            for (StatEventCommonStatus status : StatEventCommonStatus.values()) {
                if (status.getCode() == code) {
                    return status;
                }
            }
        }
        return null;
    }
}
