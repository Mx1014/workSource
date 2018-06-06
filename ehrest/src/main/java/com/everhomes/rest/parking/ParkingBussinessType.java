// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值月卡申请状态
 * <li>TEMPFEE("tempfee"),: 临时车缴费</li>
 * <li>VIP_PARKING("vipParking"),: vip车位预约</li>
 * <li>MONTH_RECHARGE("monthRecharge"),: 月卡缴费</li>
 * </ul>
 */
public enum ParkingBussinessType {
    TEMPFEE("tempfee"),
    VIP_PARKING("vipParking"),
    MONTH_RECHARGE("monthRecharge");
    private String code;

    ParkingBussinessType(String code){
        this.code = code;
    }
    public static ParkingBussinessType fromCode(String code) {
        for (ParkingBussinessType bussinessType : ParkingBussinessType.values()) {
            if (bussinessType.code.equals(code) ) {
                return bussinessType;
            }
        }
        return null;
    }
    public String getCode(){return code;}
}
