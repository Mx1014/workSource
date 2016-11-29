package com.everhomes.rest.quality;

/**
 * <p>策略：</p>
 * <ul>
 * <li>ADD(0): 增加</li>
 * <li>MODIFY(1): 修改</li>
 * <li>DELETE(2): 删除</li>
 * </ul>
 *
 */
public enum SpecificationApplyPolicy {

	ADD((byte)0), MODIFY((byte)1), DELETE((byte)2); 
    
    private byte code;
    
    private SpecificationApplyPolicy(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static SpecificationApplyPolicy fromCode(Byte code) {
        if(code != null) {
        	SpecificationApplyPolicy[] values = SpecificationApplyPolicy.values();
            for(SpecificationApplyPolicy value : values) {
                if(value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
