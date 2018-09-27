package com.everhomes.rest.archives;

/**
 * <ul>
 * <li>QUIT((byte) 0): 辞职</li>
 * <li>FIRE((byte) 1): 解雇</li>
 * <li>OTHER((byte) 2): 其他</li>
 * <li>RETIRE((byte) 3): 退休</li>
 * </ul>
 */
public enum ArchivesDismissType {
    QUIT((byte) 0), FIRE((byte) 1), OTHER((byte) 2), RETIRE((byte)3);
    private Byte code;

    ArchivesDismissType(Byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ArchivesDismissType fromCode(Byte code) {
        if(code != null) {
            ArchivesDismissType[] values = ArchivesDismissType.values();
            for(ArchivesDismissType value : values) {
                if(code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}