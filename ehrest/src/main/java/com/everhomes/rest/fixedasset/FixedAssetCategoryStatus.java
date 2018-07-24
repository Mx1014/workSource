package com.everhomes.rest.fixedasset;

/**
 * <ul>
 * <li>INVALID((byte) 0): 无效的</li>
 * <li>VALID((byte) 1): 有效的</li>
 * </ul>
 */
public enum FixedAssetCategoryStatus {
    INVALID((byte) 0), VALID((byte) 1);

    private byte code;

    FixedAssetCategoryStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static FixedAssetCategoryStatus fromCode(Byte code) {
        if (code != null) {
            FixedAssetCategoryStatus[] values = FixedAssetCategoryStatus.values();
            for (FixedAssetCategoryStatus value : values) {
                if (code.byteValue() == value.code)
                    return value;
            }
        }
        return null;
    }
}
