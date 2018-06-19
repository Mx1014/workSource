// @formatter:off
package com.everhomes.rest.module;

/**
 * <ul>
 * <li>PARK: 园区模块</li>
 * <li>ORGANIZATION: 机构企业模块</li>
 * <li>MANAGER: 运营管理方模块</li>
 * </ul>
 */
public enum AccessControlType {
    All((byte)0), logon((byte)1), auth((byte)2);

    private byte code;

    private AccessControlType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AccessControlType fromCode(Byte code) {
        if(code == null){
            return null;
        }

        for (AccessControlType type: AccessControlType.values()){
            if(type.getCode() == code.byteValue()){
                return type;
            }
        }

        return null;

    }
}
