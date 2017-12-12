// @formatter:off
package com.everhomes.rest.poll;

/**
 * <ul>
 *     <li>NO((byte) 1): NO</li>
 *     <li>YES((byte) 2): YES</li>
 * </ul>
 */
public enum PollVoterStatus {
    NO((byte) 1), YES((byte) 2);

    private Byte code;

    private PollVoterStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PollVoterStatus fromCode(Byte code) {
        PollVoterStatus[] values = PollVoterStatus.values();
        for (PollVoterStatus value : values) {
            if (Byte.valueOf(value.code).equals(code)) {
                return value;
            }
        }

        return null;
    }
}
