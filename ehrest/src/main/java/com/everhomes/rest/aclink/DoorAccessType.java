package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>ZLACLINK_WIFI((byte)0):左邻, 带 WIFI</li>
 * <li>ZLACLINK_NOWIFI((byte)1): 左邻, 不带 WIFI</li>
 * <li>ACLINK_ZL_GROUP((byte)5):左邻, 分组设备</li>
 * <li>ACLINK_LINGLING_GROUP((byte)6):令令，分组设备</li>
 * <li>ACLINK_LINGLING((byte)11):令令,开门设备</li>
 * <li>ACLINK_HUARUN_GROUP((byte)12):华润,分组设备</li>
 *  <li>ACLINK_WANGLONG: 13, 旺龙梯控</li>
 *  <li>ACLINK_WANGLONG_GROUP: 14, 旺龙梯控组</li>
 *  <li>ACLINK_WANGLONG_DOOR: 15, 旺龙门禁</li>
 *  <li>ACLINK_WANGLONG_DOOR_GROUP: 16, 旺龙门禁组</li>
 *  <li>ACLINK_UCLBRT_DOOR: 18, 锁掌柜门禁 </li>
 * <li>ACLINK_BUS((byte) 17): 园区班车</li>
 * <li>ZLACLINK_WIFI_2:19,左邻新门禁</li>
 * </ul>
 * @author janson
 *
 */
public enum DoorAccessType {
    ZLACLINK_WIFI((byte) 0), ZLACLINK_NOWIFI((byte) 1), ACLINK_ZL_GROUP((byte) 5), ACLINK_LINGLING_GROUP((byte) 6),
    ACLINK_LINGLING((byte) 11), ACLINK_HUARUN_GROUP((byte) 12), ACLINK_WANGLONG((byte) 13), ACLINK_WANGLONG_GROUP((byte) 14),
    ACLINK_WANGLONG_DOOR((byte) 15), ACLINK_WANGLONG_DOOR_GROUP((byte) 16), ACLINK_BUS((byte) 17),ACLINK_UCLBRT_DOOR((byte) 18),ZLACLINK_WIFI_2((byte) 19);

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
        case 13:
            return ACLINK_WANGLONG;
        case 14:
            return ACLINK_WANGLONG_GROUP;
        case 15:
            return ACLINK_WANGLONG_DOOR;
        case 16:
            return ACLINK_WANGLONG_DOOR_GROUP;
        case 17:
        	return ACLINK_BUS;
        }
        
        return null;
    }
}
