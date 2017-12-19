// @formatter:off
package com.everhomes.rest.relocation;

/**
 * <ul>物品搬迁通用状态
 * <li>INACTIVE(0): 无效</li>
 * <li>ACTIVE(2): 正常使用中</li>
 * </ul>
 */
public enum RelocationCommonStatus {
	INACTIVE((byte)0), ACTIVE((byte)2);

    private byte code;

    private RelocationCommonStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RelocationCommonStatus fromCode(Byte code) {
        if(code != null) {
            RelocationCommonStatus[] values = RelocationCommonStatus.values();
            for(RelocationCommonStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
