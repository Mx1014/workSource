package com.everhomes.rest.techpark.punch;

/**
 * <ul>是否被查看过flag
 * <li>notview(1):未查看过</li>
 * <li>is veiw(0): 查看过</li>
 * </ul>
 */
public enum ViewFlags {
	/*** <li>NOTVIEW(1):未查看过</li>*/
	NOTVIEW((byte)1), 
	/** * <li>ISVIEW(0): 查看过</li>*/
	ISVIEW((byte)0);
    
    private byte code;
    private ViewFlags(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ViewFlags fromCode(byte code) {
        for(ViewFlags t : ViewFlags.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
