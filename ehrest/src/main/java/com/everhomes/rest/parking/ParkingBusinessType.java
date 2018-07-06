// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值月卡申请状态
 * <li>TEMPFEE("tempfee"),: 临时车缴费</li>
 * <li>VIP_PARKING("vipParking"),: vip车位预约</li>
 * <li>MONTH_RECHARGE("monthRecharge"),: 月卡缴费</li>
 * <li>LOCK_CAR("lockCar"),: 锁车 V6.6</li>
 * <li>SEARCH_CAR("searchCar"),: 寻车 V6.6</li>
 * <li>CAR_NUM("carNum"),: 显示在场车数量 V6.6</li>
 * <li>FREE_PLACE("freePlace"),: 显示空余车位数量 V6.6</li>
 * <li> MONTH_CARD_APPLY("monthCardApply"),: 在线月卡申请 V6.6</li>
 * </ul>
 */
public enum ParkingBusinessType {
    TEMPFEE("tempfee","临时车缴费"),
    VIP_PARKING("vipParking","vip车位预约"),
    MONTH_RECHARGE("monthRecharge","月卡充值"),
    LOCK_CAR("lockCar","锁车/解锁"),
    SEARCH_CAR("searchCar","寻车"),
    CAR_NUM("carNum","在场车数量"),
    FREE_PLACE("freePlace","空余车位数量"),
    MONTH_CARD_APPLY("monthCardApply","在线月卡申请");
    private String code;
    private String desc;

    ParkingBusinessType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ParkingBusinessType fromCode(String code) {
        for (ParkingBusinessType bussinessType : ParkingBusinessType.values()) {
            if (bussinessType.code.equals(code) ) {
                return bussinessType;
            }
        }
        return null;
    }
    public String getCode(){return code;}
}
