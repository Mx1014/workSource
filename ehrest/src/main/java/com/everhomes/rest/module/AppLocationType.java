package com.everhomes.rest.module;

/**
 * <ul>
 * <li>WORKBENCH: 移动端</li>
 * <li>SERVER_SQUARE: 服务广场</li>
 * </ul>
 */
public enum AppLocationType {
    WORKBENCH((byte) 1), SERVER_SQUARE((byte) 2);

    private byte code;

    private AppLocationType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static AppLocationType fromCode(Byte code) {
        if (code == null)
            return null;

        switch (code.byteValue()) {
            case 1:
                return WORKBENCH;

            case 2:
                return SERVER_SQUARE;

            default:
                break;
        }
        return null;
    }
}
