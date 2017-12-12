// @formatter:off
package com.everhomes.rest.poll;

/**
 * <ul>
 *     <li>NO(1): NO</li>
 *     <li>YES(2): YES</li>
 * </ul>
 */
public enum PollVoterStatus {
    NO(1), YES(2);

    private int code;

    private PollVoterStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static PollVoterStatus fromCode(int code) {
        PollVoterStatus[] values = PollVoterStatus.values();
        for (PollVoterStatus value : values) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
