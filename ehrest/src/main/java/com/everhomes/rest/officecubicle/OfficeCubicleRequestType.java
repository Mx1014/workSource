// @formatter:off
package com.everhomes.rest.officecubicle;

/**
 * <ul>
 * <li>CLIENT((byte)0): 客户端请求</li>
 * <li>BACKGROUND((byte)1): 后台请求</li>
 * </ul>
 */
public enum OfficeCubicleRequestType {
	CLIENT((byte)0,"用户发起"),BACKGROUND((byte)1,"后台录入");

    private byte code;
	private String desc;

    private OfficeCubicleRequestType(byte code, String desc) {
        this.code = code;
		this.desc = desc;
    }
    
	public String getDesc(){
		return desc;
	}
	
    public byte getCode() {
        return this.code;
    }
    
    public static OfficeCubicleRequestType fromCode(Byte code) {
        if(code != null) {
        	OfficeCubicleRequestType[] values = OfficeCubicleRequestType.values();
            for(OfficeCubicleRequestType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
