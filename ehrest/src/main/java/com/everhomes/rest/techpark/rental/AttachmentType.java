package com.everhomes.rest.techpark.rental;

import com.everhomes.rest.techpark.punch.PunchStatus;
/**
 * <ul>
 * <li>STRING(0): </li>
 * <li>EMAIL(1): </li>
 * <li>FILE(2): </li>
 * </ul>
 */
public enum AttachmentType {
   
	STRING((byte)0),EMAIL((byte)1),FILE((byte)2);
    
    private byte code;
    private AttachmentType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AttachmentType fromCode(byte code) {
        for(AttachmentType t : AttachmentType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
