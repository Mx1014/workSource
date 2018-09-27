package com.everhomes.rest.module;

/**
 * <ul>
 *     <li>MOBILE((byte) 1): 移动端 </li>
 *     <li>PC((byte) 2): PC端 </li>
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
        if (code != null) {
            for (TerminalType value : TerminalType.values()) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
