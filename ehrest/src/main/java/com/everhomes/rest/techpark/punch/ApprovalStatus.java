package com.everhomes.rest.techpark.punch;

/**
 * <ul>审批后的状态
 * 
 *<li>NONENTRY(16): 未入职</li>
 *<li>RESIGNED(15): 已离职</li>
 *<li>FORGOT(14): 忘打卡</li>
 *<li>HALFOUTWORK(13):  半天公出</li>
 *<li>HALFEXCHANGE(12):  半天调休</li>
 *<li>HALFABSENCE(11):  半天病假</li>
 *<li>HALFSICK(10):  半天事假</li>
 *<li>OVERTIME(9):  加班</li>
 *<li>OUTWORK(8):  公出</li>
 * <li>EXCHANGE(7): 调休</li>
 * <li>SICK(6): 病假</li>
 * <li>ABSENCE(5): 事假</li>
 * <li>BLANDLE(4): 迟到且早退</li>
 * <li>UNPUNCH(3): 未打卡</li>
 * <li>LEAVEEARLY(2): 早退</li>
 * <li>BELATE(1): 迟到</li>
 * <li>NORMAL(0): 正常</li>
 * </ul>
 */
public enum ApprovalStatus {

	NONENTRY((byte) 16),
	RESIGNED((byte) 15),
	FORGOT((byte)14),
	HALFOUTWORK((byte)13),
	HALFEXCHANGE((byte)12),
	HALFABSENCE((byte)11),
	HALFSICK((byte)10),
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
    private ApprovalStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ApprovalStatus fromCode(byte code) {
        for(ApprovalStatus t : ApprovalStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
