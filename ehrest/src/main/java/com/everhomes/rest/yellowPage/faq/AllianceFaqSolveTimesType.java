package com.everhomes.rest.yellowPage.faq;
/**
 * <ul>排序参数
 * <li>UN_SOLVE_TIMES(0):按未解决次数排序</li> 
 * <li>SOLVE_TIMES(1): 按解决次数排序</li>
 * </ul>
 */

public enum AllianceFaqSolveTimesType {
	SOLVE_TIMES((byte)1), UN_SOLVE_TIMES((byte)0);
	    
    private Byte code;
    private AllianceFaqSolveTimesType(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static AllianceFaqSolveTimesType fromCode(Byte code) {
        for(AllianceFaqSolveTimesType t : AllianceFaqSolveTimesType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
