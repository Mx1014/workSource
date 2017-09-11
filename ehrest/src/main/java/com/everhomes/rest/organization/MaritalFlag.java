package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>UNDISCLOSURED((byte)0, "保密"): 保密</li>
 * <li>MARRIED((byte)1, "已婚"): 已婚</li>
 * <li>UNMARRIED((byte)2, "未婚"): 未婚</li>
 * <li>DIVORCE((byte)3, "离异"): 离异</li>
 * </ul>
 */
public enum MaritalFlag {
    UNDISCLOSURED((byte) 0, "保密"), MARRIED((byte) 1, "已婚"), UNMARRIED((byte) 2, "未婚"), DIVORCE((byte) 3, "离异");

    private byte code;
    private String text;

    private MaritalFlag(byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public byte getCode() {
        return this.code;
    }

    public String getText() {
        return text;
    }

    public static MaritalFlag fromCode(Byte code) {
        if (code != null) {
            MaritalFlag[] values = MaritalFlag.values();
            for (MaritalFlag value : values) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}