package com.everhomes.rest.meeting;


/**
 * <ul>
 * <li>OFF((byte) 0): FALSE</li>
 * <li>ON((byte) 1): TRUE</li>
 * </ul>
 */
public enum MeetingGeneralFlag {
    OFF((byte) 0), ON((byte) 1);

    private byte code;

    MeetingGeneralFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static MeetingGeneralFlag fromCode(Byte code) {
        if (code != null) {
            MeetingGeneralFlag[] values = MeetingGeneralFlag.values();
            for (MeetingGeneralFlag value : values) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}
