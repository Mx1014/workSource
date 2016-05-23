package com.everhomes.rest.wifi;

/**
 * <ul>
 * <li>UNACTIVE</li>
 * <li>ACTIVE </li>
 * </ul>
 */
public enum WifiSettingStatus {
	UNACTIVE((byte)0), ACTIVE((byte)1);
    
    private byte code;
    
    private WifiSettingStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static WifiSettingStatus fromCode(Byte code) {
        if(code != null) {
        	WifiSettingStatus[] values = WifiSettingStatus.values();
            for(WifiSettingStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
