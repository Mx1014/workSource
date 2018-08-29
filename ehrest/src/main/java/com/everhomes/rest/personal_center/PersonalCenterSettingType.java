package com.everhomes.rest.personal_center;

/**
 * MEMBER_LEVEL: 会员等级
 * POINT: 积分
 * WALLET： 钱包
 * ORDER：订单
 * COUPON：卡券
 * INVOICE：发票
 * MY_APPLY：我的申请
 * MY_ADDRESS：我的地址
 * MY_SHOP：我的店铺
 * MY_PUBLISH：我的发布
 * MY_COLLECT：我的收藏
 * MY_ENROLL：我的报名
 * SETTING：设置
 */
public enum PersonalCenterSettingType {
    MEMBER_LEVEL((byte)0), POINT((byte)1), WALLET((byte)2), ORDER((byte) 3), COUPON((byte) 4),
    INVOICE((byte) 5), MY_APPLY((byte) 6), MY_ADDRESS((byte) 7), MY_SHOP((byte) 8), MY_PUBLISH((byte) 9),
    MY_COLLECT((byte) 10), MY_ENROLL((byte) 11), SETTING((byte) 12);

    private Byte code;
    private PersonalCenterSettingType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static PersonalCenterSettingType fromCode(Byte code) {
        if(code != null) {
            PersonalCenterSettingType[] values = PersonalCenterSettingType.values();
            for(PersonalCenterSettingType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
