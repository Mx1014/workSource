// @formatter:off
package com.everhomes.rest.business;

/**
 * <ul>
 * <li>NONE(0): 默认</li>
 * <li>RECOMMEND(1): 推荐</li>
 * </ul>
 */
public enum BusinessRecommendStatus {
    NONE((byte)0), RECOMMEND((byte)1);
    
    private byte code;
    
    private BusinessRecommendStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static BusinessRecommendStatus fromCode(Byte code) {
        if(code == null)
            return null;
        BusinessRecommendStatus[] values = BusinessRecommendStatus.values();
        for(BusinessRecommendStatus value : values){
            if(value.getCode() == code.byteValue())
                return value;
        }
        
        return null;
    }
}
