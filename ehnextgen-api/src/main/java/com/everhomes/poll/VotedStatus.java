package com.everhomes.poll;

public enum VotedStatus {
    UNVOTED(1), VOTED(2), UNKNOWN(0);
    private Integer code;

    VotedStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public static VotedStatus fromString(String str) {
        for (VotedStatus status : VotedStatus.values()) {
            if (status.name().equalsIgnoreCase(str)) {
                return status;
            }
        }
        return UNKNOWN;
    }

    public static VotedStatus fromCode(Integer code) {
        for (VotedStatus status : VotedStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return UNKNOWN;
    }
}
