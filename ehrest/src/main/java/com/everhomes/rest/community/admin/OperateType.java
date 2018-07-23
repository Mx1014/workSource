// @formatter:off
package com.everhomes.rest.community.admin;

/**
 * <ul>审核类型
 * <li>EMAIL(0): 邮箱</li>
 * <li>MANUAL(1): 人工审核</li>
 * <li>IMPORT(2): 通讯录导入</li>
 * </ul>
 */
public enum OperateType {
    EMAIL((byte)0), MANUAL((byte)1), IMPORT((byte) 2);

    private byte code;

    private OperateType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OperateType fromCode(Byte code) {
        if(code != null) {
            OperateType[] values = OperateType.values();
            for(OperateType value : values) {
                if(code.byteValue() == value.getCode()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
