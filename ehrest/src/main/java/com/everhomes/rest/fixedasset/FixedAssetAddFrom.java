package com.everhomes.rest.fixedasset;

/**
 * <p>之所以把‘其他’放在第一个位置，是为方便以后扩展更多的值</p>
 * <ul>
 * <li>OTHERS((byte) 0, "其他"): 其他</li>
 * <li>BUY((byte) 1, "购入"): 购入</li>
 * <li>BUILD_BY_SELF((byte) 2, "自建"): 自建</li>
 * <li>LEASE((byte) 3, "租赁"): 租赁</li>
 * <li>CONTRIBUTE((byte) 4, "捐赠"): 捐赠</li>
 * </ul>
 */
public enum FixedAssetAddFrom {
    OTHERS((byte) 0, "其他"), BUY((byte) 1, "购入"), BUILD_BY_SELF((byte) 2, "自建"), LEASE((byte) 3, "租赁"), CONTRIBUTE((byte) 4, "捐赠");

    private byte code;
    private String name;

    FixedAssetAddFrom(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static FixedAssetAddFrom fromCode(Byte code) {
        if (code != null) {
            FixedAssetAddFrom[] values = FixedAssetAddFrom.values();
            for (FixedAssetAddFrom value : values) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }

    public static FixedAssetAddFrom fromName(String name) {
        if (name != null) {
            FixedAssetAddFrom[] values = FixedAssetAddFrom.values();
            for (FixedAssetAddFrom value : values) {
                if (name.equals(value.getName())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static String printAll() {
        StringBuffer str = new StringBuffer(20);
        FixedAssetAddFrom[] values = FixedAssetAddFrom.values();
        for (FixedAssetAddFrom value : values) {
            if (FixedAssetAddFrom.OTHERS == value) {
                // ‘其他’选项放在最后
                continue;
            }
            str.append(value.getName()).append('、');
        }
        str.append(FixedAssetAddFrom.OTHERS.getName());
        return str.toString();
    }
}
