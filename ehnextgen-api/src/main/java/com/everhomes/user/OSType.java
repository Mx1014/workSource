package com.everhomes.user;

public enum OSType {
    Unknown((byte) 0), IOS((byte) 1), Android((byte) 2), windowsPhone((byte) 3), Firefox((byte) 4), Blackberry((byte) 5), IOSWeiXin((byte) 6), AndriodWeiXin((byte) 7);
    private Byte code;

    OSType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static OSType fromString(String val) {
        for (OSType os : OSType.values()) {
            if (os.name().equalsIgnoreCase(val)) {
                return os;
            }

        }
        return Unknown;
    }

    public static OSType fromCode(String code) {
        for (OSType os : OSType.values()) {
            if (os.getCode().toString().equals(code)) {
                return os;
            }
        }
        return Unknown;
    }
}
