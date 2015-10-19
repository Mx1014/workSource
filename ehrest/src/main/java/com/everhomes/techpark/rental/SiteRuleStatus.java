package com.everhomes.techpark.rental;

import com.everhomes.techpark.punch.PunchStatus;
/**
 * <ul>
 * <li>OPEN(0): </li>
 * <li>CLOSE(1): </li>
 * </ul>
 */
public enum SiteRuleStatus {
   
    OPEN((byte)0),CLOSE((byte)1);
    
    private byte code;
    private SiteRuleStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static SiteRuleStatus fromCode(byte code) {
        for(SiteRuleStatus t : SiteRuleStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
