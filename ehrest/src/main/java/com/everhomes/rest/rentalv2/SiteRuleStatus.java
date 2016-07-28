package com.everhomes.rest.rentalv2;

import com.everhomes.rest.techpark.punch.PunchStatus;
/**
 * <ul>
 * <li>OPEN(0): 可以预定</li>
 * <li>CLOSE(1): 场所被订完了！</li>
 * <li>EARLY(2): 太早了，还不能预订 </li>
 * <li>LATE(3): 太晚了过了预定时间</li>
 * </ul>
 */
public enum SiteRuleStatus {
   
    OPEN((byte)0),CLOSE((byte)1),EARLY((byte)2),LATE((byte)3);
    
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
