// @formatter:off
package com.everhomes.rest.group;

/**
 * <ul>发帖标记
 * <li>ALL: 所有人可以发帖</li>
 * <li>ADMIN_ONLY: 仅管理员可以发帖</li>
 * </ul>
 */
public enum GroupPostFlag {
    ALL((byte)0), ADMIN_ONLY((byte)1);
    
    private byte code;
    
    private GroupPostFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static GroupPostFlag fromCode(Byte code) {
        if(code != null) {
            GroupPostFlag[] values = GroupPostFlag.values();
            for(GroupPostFlag value : values) {
                if(value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
