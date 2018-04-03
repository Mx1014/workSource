package com.everhomes.rest.organization;

/**
 * <p>操作类型</p>
 * <ul>
 * <li>QUIT: 0-退出</li> 
 * <li>JOIN: 1-加入</li> 
 * </ul>
 */
public enum OperationType {

	QUIT((byte)0), JOIN((byte)1);
	
	private byte code;
    private OperationType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OperationType fromCode(byte code) {
    	OperationType[] values = OperationType.values();
        for(OperationType value : values) {
            if(value.code == code) {
                return value;
            }
        }
        
        return null;
    }
}
