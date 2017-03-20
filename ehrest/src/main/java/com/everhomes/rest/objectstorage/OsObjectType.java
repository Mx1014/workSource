// @formatter:off
package com.everhomes.rest.objectstorage;

/**
 * <ul>
 *     <li>DIR(0): 目录</li>
 *     <li>FILE(1): 文件</li>
 * </ul>
 */
public enum OsObjectType {

    DIR((byte)0), FILE((byte)1);

    private Byte code;

    OsObjectType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static OsObjectType fromCode(Byte code) {
        if (code != null) {
            for (OsObjectType type : OsObjectType.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
