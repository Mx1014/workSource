package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

public enum QualityModelType {
    //后面再扩展
    STANDARD((byte) 0),SPECIFICATION((byte)1), CATEGORY((byte)2);

    private byte code;

    QualityModelType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public static QualityModelType fromStatu(byte code) {
        for (QualityModelType v : QualityModelType.values()) {
            if (v.code == code) {
                return v;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
