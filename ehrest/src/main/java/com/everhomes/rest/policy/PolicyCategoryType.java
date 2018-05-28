package com.everhomes.rest.policy;



/**
 * <ul>参数类型
 * <li>OTHER(0): 其他</li>
 * <li>TECH(1): 科技类</li>
 * <li>CONSTRUCT(2): 建筑类</li>
 * <li>MANU(3): 制造类</li>
 * <li>EMERGING(4): 新兴产业</li>
 * </ul>
 */
public enum PolicyCategoryType {

    OTHER((byte)0),TECH((byte)1),CONSTRUCT((byte)2),MANU((byte)3),EMERGING((byte)4);

    private byte code;

    PolicyCategoryType(byte code) {
        this.code = code;
    }

    public static PolicyCategoryType fromCode(Byte code){
        if(code != null) {
            PolicyCategoryType[] values = PolicyCategoryType.values();
            for(PolicyCategoryType value : values) {
                if(code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }
}
