package com.everhomes.rest.personal_center;

/**
 * INACTIVE:失效
 * SAVING: 保存
 * ACTIVE： 生效
 */
public enum PersonalCenterSettingStatus {
    INACTIVE((byte)0), SAVING((byte)1), ACTIVE((byte)2);

    private Byte code;
    private PersonalCenterSettingStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static PersonalCenterSettingStatus fromCode(Byte code) {
        if(code != null) {
            PersonalCenterSettingStatus[] values = PersonalCenterSettingStatus.values();
            for(PersonalCenterSettingStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
