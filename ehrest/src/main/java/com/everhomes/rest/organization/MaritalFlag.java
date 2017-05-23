package com.everhomes.rest.organization;

/**
 * <ul>group公有和私用标记
 * <li>PUBLIC: 公有，应用于兴趣圈</li>
 * <li>PRIVATE: 私有，应用于私有邻居圈</li>
 * </ul>
 */
public enum MaritalFlag {
    UNDISCLOSURED((byte)0, "保密"), MARRIED((byte)1, "已婚"), UNMARRIED((byte)2, "未婚");

    private byte code;
    private String text;

    private MaritalFlag(byte code,String text) {
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
        if(code != null) {
            MaritalFlag[] values = MaritalFlag.values();
            for(MaritalFlag value : values) {
                if(code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}