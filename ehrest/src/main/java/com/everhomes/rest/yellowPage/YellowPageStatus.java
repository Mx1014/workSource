package com.everhomes.rest.yellowPage;


/**
 * <ul>黄页状态
 * <li>INACTIVE(0): 无效的</li>
 * <li>WAITING_FOR_APPROVAL(1): 等待批准加入</li> 
 * <li>ACTIVE(2): 正常</li>
 * </ul>
 */

public enum YellowPageStatus {
	  INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1),  ACTIVE((byte)2);
	    
    private byte code;
    private YellowPageStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static YellowPageStatus fromCode(byte code) {
        for(YellowPageStatus t : YellowPageStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
