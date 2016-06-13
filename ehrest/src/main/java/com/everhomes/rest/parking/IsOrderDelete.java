// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值订单是否删除
 * <li>NOTDELETE(0): 未删除</li>
 * <li>DELETE(1): 已删除</li>
 * </ul>
 */
public enum IsOrderDelete {
    NOTDELETED((byte)0), DELETED((byte)1);
    
    private byte code;
    
    private IsOrderDelete(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static IsOrderDelete fromCode(Byte code) {
        if(code != null) {
            IsOrderDelete[] values = IsOrderDelete.values();
            for(IsOrderDelete value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
