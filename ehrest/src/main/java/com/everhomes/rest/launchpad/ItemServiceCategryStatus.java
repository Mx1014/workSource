// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>
 * <li>INACTIVE(0): 无效</li>
 * <li>ACTIVE(1): 有效</li>
 * </ul>
 */
public enum ItemServiceCategryStatus {
    INACTIVE((byte)0), ACTIVE((byte)1);

    private byte code;

    private ItemServiceCategryStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ItemServiceCategryStatus fromCode(Byte code) {
        if(null == code){
            return null;
        }
        ItemServiceCategryStatus[] values = ItemServiceCategryStatus.values();
        for(ItemServiceCategryStatus value : values) {
            if(value.code == code.byteValue()) {
                return value;
            }
        }
        return null;
    }
}
