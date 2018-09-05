package com.everhomes.rest.personal_center;

/**
 * BASIC: 基础信息区
 * BLOCK: 方块展示区
 * LIST： 列表展示区
 */
public enum PersonalCenterSettingRegionType {
    BASIC((byte)0), BLOCK((byte)1), LIST((byte)2);

    private Byte code;
    private PersonalCenterSettingRegionType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static PersonalCenterSettingRegionType fromCode(Byte code) {
        if(code != null) {
            PersonalCenterSettingRegionType[] values = PersonalCenterSettingRegionType.values();
            for(PersonalCenterSettingRegionType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
