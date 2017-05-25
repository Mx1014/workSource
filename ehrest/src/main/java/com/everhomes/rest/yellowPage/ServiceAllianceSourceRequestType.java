// @formatter:off
package com.everhomes.rest.yellowPage;

/**
 * 
 * <ul>
 * <li>CLIENT 0 : 客户端请求</li>
 * <li>CLIENT 1 : 浏览器请求</li>
 * </ul>
 *
 *  @author:dengs 2017年4月28日
 */
public enum ServiceAllianceSourceRequestType {
	CLIENT((byte)0), BROWSER((byte) 1);
	
	private byte code;
    private ServiceAllianceSourceRequestType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ServiceAllianceSourceRequestType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return CLIENT;
            
        case 1 :
            return BROWSER;
            
        default :
            break;
        }
        return null;
    }
}
