package com.everhomes.rest.techpark.punch;

/**
 * <ul> 
 * <li>WIFI(1): WiFi二维码</li>
 * <li>POINT(0): 地点二维码</li>
 * </ul>
 */
public enum QRType {
    POINT((byte)0), WIFI((byte)1);
    
    private byte code;
    private QRType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static QRType fromCode(byte code) {
        for(QRType t : QRType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
