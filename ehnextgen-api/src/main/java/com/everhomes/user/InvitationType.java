package com.everhomes.user;

public enum InvitationType {
    SMS((byte) 1), wechat((byte) 2), friend_circle((byte) 3), weibo((byte) 4), phone((byte) 5);
    private Byte code;

    InvitationType(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static InvitationType fromString(String code) {
        for (InvitationType type : InvitationType.values()) {
            if (type.name().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
    
    public static InvitationType fromCode(Byte code) {
        for (InvitationType type : InvitationType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
