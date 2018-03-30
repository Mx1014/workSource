package com.everhomes.rest.module;

/**
 * <ul>
 * <li>MOBILE: 移动端</li>
 * <li>PC: PC端</li>
 * </ul>
 */
public enum TerminalType {
    MOBILE((byte) 1), PC((byte) 2);

    private byte code;

    private TerminalType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static TerminalType fromCode(Byte code) {
        if (code == null)
            return null;

        switch (code.byteValue()) {
            case 1:
                return MOBILE;

            case 2:
                return PC;

            default:
                break;
        }
        return null;
    }
}
