package com.everhomes.rest.archives;

import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>QUIT((byte) 1): 辞职</li>
 * <li>FIRE((byte) 2): 解雇</li>
 * <li>OTHER((byte) 3): 其它</li>
 * </ul>
 */
public enum DismissType {
    QUIT((byte) 1), FIRE((byte) 2), OTHER((byte) 3);
    private Byte code;

    private DismissType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public static LayoutType fromCode(Byte code) {
        if (code != null) {
            for (LayoutType a : LayoutType.values()) {
                if (code.byteValue() == a.getCode().byteValue()) {
                    return a;
                }
            }
        }
        return null;
    }
}