// @formatter:off
package com.everhomes.rest.community.admin;

/**
 * <ul>审核类型
 * <li>MANUAL(1): 人工审核</li>
 * <li>EMAIL(2): 邮箱认证</li>
 * <li>IMPORT(3): 通讯录导入</li>
 * </ul>
 */
public enum OperateType {
    MANUAL((byte)1), EMAIL((byte)2), IMPORT((byte)3);

    private Byte code;

    private OperateType(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
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
