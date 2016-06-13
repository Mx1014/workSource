package com.everhomes.rest.wifi;

/**
 * <ul>
 * <li>SUCCESS</li>
 * <li>FAIL </li>
 * </ul>
 */
public enum VerifyWifiStatus {
	SUCCESS((byte)1), FAIL((byte)0);
    
    private byte code;
    
    private VerifyWifiStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static VerifyWifiStatus fromCode(Byte code) {
        if(code != null) {
        	VerifyWifiStatus[] values = VerifyWifiStatus.values();
            for(VerifyWifiStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
