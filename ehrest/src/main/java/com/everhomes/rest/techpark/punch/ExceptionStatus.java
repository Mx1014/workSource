package com.everhomes.rest.techpark.punch;

/**
 * <ul>异常状态
 * <li>EXCEPTION(1):异常</li>
 * <li>NORMAL(0): 正常</li>
 * </ul>
 */
public enum ExceptionStatus {
	/*** <li>BELATE(1): 迟到</li>*/
	EXCEPTION((byte)1), 
	/** * <li>NORMAL(0): 正常</li>*/
	NORMAL((byte)0);
    
    private byte code;
    private ExceptionStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ExceptionStatus fromCode(Byte code) {
        if(null == code){
            return null;
        }
        for(ExceptionStatus t : ExceptionStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
