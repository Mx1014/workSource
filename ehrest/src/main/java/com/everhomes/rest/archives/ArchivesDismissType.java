package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>QUIT((byte) 0): 辞职</li>
 * <li>FIRE((byte) 1): 解雇</li>
 * <li>OTHER((byte) 2): 其它</li>
 * </ul>
 */
public enum ArchivesDismissType {
    QUIT((byte) 0), FIRE((byte) 1), OTHER((byte) 2);
    private Byte code;

    private ArchivesDismissType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public static ArchivesDismissType fromCode(Byte code) {
        if (code != null) {
            for (ArchivesDismissType a : ArchivesDismissType.values()) {
                if (code.byteValue() == a.getCode().byteValue()) {
                    return a;
                }
            }
        }
        return null;
    }
}