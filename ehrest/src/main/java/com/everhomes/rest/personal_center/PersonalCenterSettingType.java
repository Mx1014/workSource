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
    MEMBER_LEVEL(0), POINT(1), WALLET(2), ORDER(3), COUPON(4),
    INVOICE(5), MY_APPLY(6), MY_ADDRESS( 7), MY_SHOP(8), MY_PUBLISH(9),
    MY_COLLECT(10), MY_ENROLL(11), SETTING(12);

    private Integer code;
    private PersonalCenterSettingType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public static PersonalCenterSettingType fromCode(Integer code) {
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
