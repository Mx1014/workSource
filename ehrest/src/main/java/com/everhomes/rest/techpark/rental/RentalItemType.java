package com.everhomes.rest.techpark.rental;

import com.everhomes.rest.techpark.punch.PunchStatus;
/**
 * <ul>
 * <li>SALE(1):售卖商品 </li>
 * <li>RENTAL(2): 租赁商品</li>
 * </ul>
 */
public enum RentalItemType {
   
	SALE((byte)1),RENTAL((byte)2);
    
    private byte code;
    private RentalItemType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RentalItemType fromCode(byte code) {
        for(RentalItemType t : RentalItemType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
