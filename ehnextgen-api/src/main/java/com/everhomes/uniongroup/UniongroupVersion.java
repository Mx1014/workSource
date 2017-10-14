package com.everhomes.uniongroup;

public enum UniongroupVersion {
    CURRENT(0), TEMP(-1);

    private Integer code;

    private UniongroupVersion(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static UniongroupVersion fromCode(Integer code) {
        if (code != null) {
            UniongroupVersion[] values = UniongroupVersion.values();
            for (UniongroupVersion value : values) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}
