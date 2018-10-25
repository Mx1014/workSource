package com.everhomes.yellowPage.faq;
/**
 * <ul>排序参数
 * <li>SOLVE_TIMES(0): 按解决次数排序</li>
 * <li>UN_SOLVE_TIMES(1):按未解决次数排序</li> 
 * </ul>
 */

public enum AllianceFaqOrderType {
	SOLVE_TIMES((byte)0), UN_SOLVE_TIMES((byte)1);
	    
    private Byte code;
    private AllianceFaqOrderType(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static AllianceFaqOrderType fromCode(Byte code) {
        for(AllianceFaqOrderType t : AllianceFaqOrderType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
