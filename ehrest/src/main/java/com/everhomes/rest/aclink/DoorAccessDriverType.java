package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>zuolin: 左邻二维码</li>
 * <li>lingling: 令令二维码</li>
 * <li>phone_visit: 保安手机授权</li>
 * <li>huarun_anguan: 华润安冠二维码</li>
 * <li>wanglong: 旺龙梯控</li>
 * <li>uclbrt: 锁掌柜(对接系统)</li>
 * <li>bus:园区班车</li>
 * <li>FACEPLUSPLUS("face++"):face++门禁</li>
 * </ul>
 * @author janson
 *
 */
public enum DoorAccessDriverType {
    ZUOLIN("zuolin"), LINGLING("lingling"), PHONE_VISIT("phone_visit"), ZUOLIN_V2("zuolin_v2"), HUARUN_ANGUAN("huarun_anguan"), WANG_LONG("wanglong"),
    UCLBRT("uclbrt") ,BUS("bus"), FACEPLUSPLUS("face++");

    private String code;
    private DoorAccessDriverType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static DoorAccessDriverType fromCode(String code) {
        DoorAccessDriverType[] values = DoorAccessDriverType.values();
        for(DoorAccessDriverType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
