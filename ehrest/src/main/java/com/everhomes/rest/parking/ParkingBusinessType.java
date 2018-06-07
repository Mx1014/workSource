// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值月卡申请状态
 * <li>TEMPFEE("tempfee"),: 临时车缴费</li>
 * <li>VIP_PARKING("vipParking"),: vip车位预约</li>
 * <li>MONTH_RECHARGE("monthRecharge"),: 月卡缴费</li>
 * </ul>
 */
public enum ParkingBusinessType {
    TEMPFEE("tempfee"),
    VIP_PARKING("vipParking"),
    MONTH_RECHARGE("monthRecharge");
    private String code;

    ParkingBusinessType(String code){
        this.code = code;
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
