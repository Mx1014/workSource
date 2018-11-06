package com.everhomes.rest.buttscript;

/**
 * <ul>
 *     <li>((byte) 0):FALSE同步 </li>
 *     <li>((byte) 1): TRUE异步</li>
 * </ul>
 */
public enum SyncCode {

    FALSE((byte) 0),
    TRUE((byte) 1);

    private Byte code;

    SyncCode(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static SyncCode fromCode(Byte code) {
        if (code != null) {
            for (SyncCode type : SyncCode.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
