package com.everhomes.fixedasset;

public enum FixedAssetOperationType {

    CREATE("新建"), UPDATE("编辑"), DELETE("删除");

    private String code;

    FixedAssetOperationType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static FixedAssetOperationType fromCode(String code) {
        if (code != null) {
            FixedAssetOperationType[] values = FixedAssetOperationType.values();
            for (FixedAssetOperationType value : values) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
