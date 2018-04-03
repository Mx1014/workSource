// @formatter:off
package com.everhomes.rest.group;

/**
 * <ul>name是否是empty
 * <li>0: 非空</li>
 * <li>1: 空</li>
 * </ul>
 */
public enum GroupNameEmptyFlag {
    NO_EMPTY((byte)0), EMPTY((byte)1);

    private byte code;

    private GroupNameEmptyFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static GroupNameEmptyFlag fromCode(Byte code) {
        if(code != null) {
            GroupNameEmptyFlag[] values = GroupNameEmptyFlag.values();
            for(GroupNameEmptyFlag value : values) {
                if(value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
