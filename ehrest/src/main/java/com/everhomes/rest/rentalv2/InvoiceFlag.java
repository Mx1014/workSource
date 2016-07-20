package com.everhomes.rest.rentalv2;

import com.everhomes.rest.techpark.punch.PunchStatus;
/**
 * <ul>
 * <li>NONEED(0): </li>
 * <li>NEED(1): </li>
 * </ul>
 */
public enum InvoiceFlag {
   
	NEED((byte)1),NONEED((byte)0);
    
    private byte code;
    private InvoiceFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static InvoiceFlag fromCode(byte code) {
        for(InvoiceFlag t : InvoiceFlag.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
