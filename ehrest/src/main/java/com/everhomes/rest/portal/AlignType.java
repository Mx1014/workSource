// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>align样式
 * <li>LEFT("left")：靠左</li>
 * <li>CENTER("center"): 居中</li>
 * </ul>
 */
public enum AlignType {

    LEFT("left"),
    CENTER("center");

    private String code;

    private AlignType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static AlignType fromCode(String code) {
        if(null != code){
            for (AlignType value: AlignType.values()) {
                if(value.code.equals(code)){
                    return value;
                }
            }
        }
        return null;
    }
}
