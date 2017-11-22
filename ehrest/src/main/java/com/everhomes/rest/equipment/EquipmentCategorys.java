package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

public enum EquipmentCategorys {
    FIRE_CONTROL(200887L, "消防"), STRONG(200888L, "强电"), WEAK(200889L, "弱电"), ELEVATOR(202224L, "电梯"),
    AIR_CONDITIONER(202222L, "空调"), WATER(202223L, "给排水"), ROOM(203014L, "空置房"),TE(203015L,"装修"),SAFE(203016L,"安保"),
    DAILY_CHECK(203017L,"日常工作检查"),COMMON_CHECK(203018L,"公共设施检查"),WEEEKDAY(203019L,"周末值班"),SAFTY_CHECK(203020L,"安全检查"),OTHERS(200890L,"其他");

    private Long code;
    private String name;

    EquipmentCategorys(Long code, String name) {
        this.code = code;
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static EquipmentCategorys fromStatus(byte code) {
        for(EquipmentCategorys v : EquipmentCategorys.values()) {
            if(v.getCode() == code)
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
