package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>ZLACLINK_WIFI: 0, 带 WIFI</li>
 * <li>ZLACLINK_NOWIFI: 1, 不带 WIFI</li>
 * <li>ACLINK_GROUP: 5, 分组设备</li>
 * <li>ACLINK_LINGLING: 6, 令令开门设备</li>
 * </ul>
 * @author janson
 *
 */
public enum DoorAccessType {
    ZLACLINK_WIFI((byte)0), ZLACLINK_NOWIFI((byte)1), ACLINK_ZL_GROUP((byte)5), ACLINK_LINGLING_GROUP((byte)6), ACLINK_LINGLING((byte)11), ACLINK_HUARUN_GROUP((byte)12);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private DoorAccessType(byte code) {
        this.code = code;
    }
    
    public static DoorAccessType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return ZLACLINK_WIFI;
        case 1 :
            return ZLACLINK_NOWIFI;
        case 5:
            return ACLINK_ZL_GROUP;
        case 6:
            return ACLINK_LINGLING_GROUP;
        case 11:
            return ACLINK_LINGLING;
        case 12:
        	return ACLINK_HUARUN_GROUP;
        }
        
        return null;
    }
}
