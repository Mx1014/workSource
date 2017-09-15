package com.everhomes.rest.techpark.punch;

import com.everhomes.rest.techpark.punch.PunchStatus;
/**
 * <ul>
 * <li>NO_HOMMIZATION(0): 不人性化 </li>
 * <li>FLEX(1): 弹性打卡</li>
 * <li>LATEARRIVE_LATELEAVE(2): 晚到晚走</li>
 * </ul>
 */
public enum PunchHommizationType {
   
	NO_HOMMIZATION((byte)0),FLEX((byte)1),LATEARRIVE_LATELEAVE((byte)1);
    
    private byte code;
    private PunchHommizationType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PunchHommizationType fromCode(byte code) {
        for(PunchHommizationType t : PunchHommizationType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
