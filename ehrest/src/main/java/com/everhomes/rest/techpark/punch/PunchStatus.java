package com.everhomes.rest.techpark.punch;

/**
 * <ul>打卡的状态
 *<li>NONENTRY(16): 未入职</li>
 *<li>RESIGNED(15): 已离职</li>
 *<li>FORGOT(14): 缺卡</li>
 * <li>BLANDLE(4): 迟到且早退</li>
 * <li>UNPUNCH(3): 未打卡</li>
 * <li>LEAVEEARLY(2): 早退</li>
 * <li>BELATE(1): 迟到</li>
 * <li>NORMAL(0): 正常</li>
 * </ul>
 */
public enum PunchStatus {
	NONENTRY((byte) 16),
	RESIGNED((byte) 15),
	FORGOT((byte)14),
	OVERTIME((byte)9),
	OUTWORK((byte)8),
	EXCHANGE((byte)7),
	/**<li>SICK(6): 病假</li>*/
	SICK((byte)6),
	/**<li>ABSENCE(5): 事假</li>*/
	ABSENCE((byte)5),
	/**BLANDLE(4): 迟到且早退*/
	BLANDLE((byte)4),
	/**UNPUNCH(3): 未打卡*/
	UNPUNCH((byte)3),
	/**LEAVEEARLY(2): 早退*/
	LEAVEEARLY((byte)2), 
	/*** <li>BELATE(1): 迟到</li>*/
	BELATE((byte)1), 
	/** * <li>NORMAL(0): 正常</li>*/
	NORMAL((byte)0);
    
    private byte code;
    private PunchStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PunchStatus fromCode(byte code) {
        for(PunchStatus t : PunchStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
