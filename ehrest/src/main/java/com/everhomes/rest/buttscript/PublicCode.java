package com.everhomes.rest.buttscript;

/**
 * <ul>
 *     <li>((byte) 0):FALSE </li>
 *     <li>((byte) 1): TRUE</li>
 * </ul>
 */
public enum PublicCode {

    FALSE((byte) 0),
    TRUE((byte) 1);

    private Byte code;

    PublicCode(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static PublicCode fromCode(Byte code) {
        if (code != null) {
            for (PublicCode type : PublicCode.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
