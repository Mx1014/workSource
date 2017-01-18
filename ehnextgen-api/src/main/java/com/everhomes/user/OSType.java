package com.everhomes.user;

public enum OSType {
    Android((byte) 2), windowsPhone((byte) 3), IOS((byte) 1), Firefox((byte) 4), Blackberry((byte) 5), Unknown((byte) 0);
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
