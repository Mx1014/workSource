// @formatter:off
package com.everhomes.rest.community.admin;

/**
 * <ul>审核类型
 * <li>NOT_MANUAL(0): 非人工审核(邮箱认证或通讯录导入)</li>
 * <li>MANUAL(1): 人工审核</li>
 * </ul>
 */
public enum OperateType {
    NOT_MANUAL((byte)0), MANUAL((byte)1);

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
