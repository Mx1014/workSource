package com.everhomes.rest.fixedasset;

/**
 * <ul>
 * <li>UNUSED((byte) 1, "闲置"): 闲置</li>
 * <li>IN_USE((byte) 2, "使用中"): 使用中</li>
 * <li>IN_MAINTENANCE((byte) 3, "维修中"): 维修中</li>
 * <li>ALREADY_SOLD((byte) 4, "已出售"): 已出售</li>
 * <li>USELESS((byte) 5, "已报废"): 已报废</li>
 * <li>LOSS((byte) 6, "遗失"): 遗失</li>
 * </ul>
 */
public enum FixedAssetStatus {
    UNUSED((byte) 1, "闲置"), IN_USE((byte) 2, "使用中"), IN_MAINTENANCE((byte) 3, "维修中"), ALREADY_SOLD((byte) 4, "已出售"), USELESS((byte) 5, "已报废"), LOSS((byte) 6, "遗失");

    private byte code;
    private String name;

    FixedAssetStatus(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static FixedAssetStatus fromCode(Byte code) {
        if (code != null) {
            FixedAssetStatus[] values = FixedAssetStatus.values();
            for (FixedAssetStatus value : values) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }

    public static FixedAssetStatus fromName(String name) {
        if (name != null) {
            FixedAssetStatus[] values = FixedAssetStatus.values();
            for (FixedAssetStatus value : values) {
                if (name.equals(value.getName())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static String printAll() {
        StringBuffer str = new StringBuffer(30);
        FixedAssetStatus[] values = FixedAssetStatus.values();
        for (FixedAssetStatus value : values) {
            str.append(value.getName()).append('、');
        }
        String s = str.toString();
        if (s.endsWith("、")) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }
}
