// @formatter:off
package com.everhomes.rest.community.admin;

/**
 * <ul>审核类型
 * <li>NO(0): 没有审核权限</li>
 * <li>YES(1): 有审核权限</li>
 * </ul>
 */
public enum CheckAuditingType {
    NO((byte)0), YES((byte)1);

    private byte code;

    private CheckAuditingType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static CheckAuditingType fromCode(Byte code) {
        if(code != null) {
            CheckAuditingType[] values = CheckAuditingType.values();
            for(CheckAuditingType value : values) {
                if(code.byteValue() == value.getCode()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
