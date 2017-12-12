package com.everhomes.rest.statistics.event;

/**
 * <ul>
 *     <li>STRING((byte) 1): STRING</li>
 *     <li>NUMBER((byte) 2): NUMBER</li>
 * </ul>
 */
public enum StatEventParamType {

    STRING((byte) 1), NUMBER((byte) 2);

    private byte code;

    StatEventParamType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static StatEventParamType fromCode(Byte code) {
        if (code != null) {
            for (StatEventParamType status : StatEventParamType.values()) {
                if (status.getCode() == code) {
                    return status;
                }
            }
        }
        return null;
    }
}
