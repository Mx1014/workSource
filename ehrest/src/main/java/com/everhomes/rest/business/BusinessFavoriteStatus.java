// @formatter:off
package com.everhomes.rest.business;

/**
 * <ul>
 * <li>NONE(0): 默认</li>
 * <li>FAVORITE(1): 已收藏</li>
 * </ul>
 */
public enum BusinessFavoriteStatus {
    NONE((byte)0), FAVORITE((byte)1);
    
    private byte code;
    
    private BusinessFavoriteStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static BusinessFavoriteStatus fromCode(Byte code) {
        if(code == null)
            return null;
        BusinessFavoriteStatus[] values = BusinessFavoriteStatus.values();
        for(BusinessFavoriteStatus value : values){
            if(value.getCode() == code.byteValue())
                return value;
        }
        
        return null;
    }
}
