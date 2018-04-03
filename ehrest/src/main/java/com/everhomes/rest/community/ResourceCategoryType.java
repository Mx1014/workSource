// @formatter:off
package com.everhomes.rest.community;

/**
 * <ul>分类类型
 * <li>CATEGORY(1): 分类</li>
 * <li>OBJECT(2): 项目</li>
 * </ul>
 */
public enum ResourceCategoryType {
    CATEGORY((byte)1), OBJECT((byte)2);

    private byte code;

    private ResourceCategoryType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ResourceCategoryType fromCode(Byte code) {
        if(code != null) {
            ResourceCategoryType[] values = ResourceCategoryType.values();
            for(ResourceCategoryType value : values) {
                if(code.byteValue() == value.getCode()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
