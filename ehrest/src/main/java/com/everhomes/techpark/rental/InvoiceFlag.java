package com.everhomes.techpark.rental;

import com.everhomes.techpark.punch.PunchStatus;
/**
 * <ul>
 * <li>NEED(0): </li>
 * <li>NONEED(1): </li>
 * </ul>
 */
public enum InvoiceFlag {
   
	NEED((byte)0),NONEED((byte)1);
    
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
