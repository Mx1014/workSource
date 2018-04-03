package com.everhomes.rest.organization;

/**
 * <p>查询子岗位是否是简单模式</p>
 * <ul>
 * <li>NO: 非简单模式</li>
 * <li>YES: 简单模式</li>
 * </ul>
 */

public enum OrganizationChildrenJobPositionSimpleFlag {
    NO((byte) 0), YES((byte) 1);

    private byte code;

    private OrganizationChildrenJobPositionSimpleFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static OrganizationChildrenJobPositionSimpleFlag fromCode(Byte code) {
        if (code != null) {
            for (OrganizationChildrenJobPositionSimpleFlag value : OrganizationChildrenJobPositionSimpleFlag.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
