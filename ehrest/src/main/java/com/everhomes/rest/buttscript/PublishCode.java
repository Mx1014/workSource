package com.everhomes.rest.buttscript;

/**
 * <ul>
 *     <li>((byte) 0):FALSE </li>
 *     <li>((byte) 1): TRUE</li>
 * </ul>
 */
public enum PublishCode {

    FALSE((byte) 0),
    TRUE((byte) 1);

    private Byte code;

    PublishCode(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static PublishCode fromCode(Byte code) {
        if (code != null) {
            for (PublishCode type : PublishCode.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
