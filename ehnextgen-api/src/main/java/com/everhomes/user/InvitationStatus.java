package com.everhomes.user;

public enum InvitationStatus {
    inactive((byte) 1), active((byte) 2);
    private Byte code;

    InvitationStatus(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static InvitationStatus fromString(String code) {
        for (InvitationStatus status : InvitationStatus.values()) {
            if (status.name().equalsIgnoreCase(code)) {
                return status;
            }
        }
        return null;
    }
}
