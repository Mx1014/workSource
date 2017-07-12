package com.everhomes.rest.organization;

/**
 * <p>操作类型</p>
 * <ul>
 * <li>ADMIN: 0-管理员操作</li> 
 * <li>USER: 1-用户自己操作</li> 
 * </ul>
 */
public enum RequestType {

	ADMIN((byte)0), USER((byte)1);
	
	private byte code;
    private RequestType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RequestType fromCode(byte code) {
    	RequestType[] values = RequestType.values();
        for(RequestType value : values) {
            if(value.code == code) {
                return value;
            }
        }
        
        return null;
    }
}
