package com.everhomes.uniongroup;

public enum UniongroupVersionEnum {
    CURRENT(0), TEMP(-1);

    private Integer code;

    private UniongroupVersionEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static UniongroupVersionEnum fromCode(Integer code) {
        if (code != null) {
            UniongroupVersionEnum[] values = UniongroupVersionEnum.values();
            for (UniongroupVersionEnum value : values) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}
