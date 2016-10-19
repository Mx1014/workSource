// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>
 * <li>LEFT(0): 居左</li>
 * <li>CENTER(1): 居中</li>
 * </ul>
 */
public enum ItemServiceCategryAlign {
    LEFT((byte)0), CENTER((byte)1);

    private byte code;

    private ItemServiceCategryAlign(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ItemServiceCategryAlign fromCode(Byte code) {
        if(null == code){
            return null;
        }
        ItemServiceCategryAlign[] values = ItemServiceCategryAlign.values();
        for(ItemServiceCategryAlign value : values) {
            if(value.code == code.byteValue()) {
                return value;
            }
        }
        return null;
    }
}
