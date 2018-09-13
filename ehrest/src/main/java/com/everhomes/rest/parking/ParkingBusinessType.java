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
 * <li>USER_NOTICE("userNotice"),: 用户须知</li>
 * <li>INVOICE_APPLY("invoiceApply"),: 发票申请</li>
 * </ul>
 */
public enum ParkingBusinessType {
    TEMPFEE("tempfee","临时车缴费","getTempfeeFlag","setTempfeeFlag"),
    VIP_PARKING("vipParking","vip车位预约","getVipParkingFlag","setVipParkingFlag"),
    MONTH_RECHARGE("monthRecharge","月卡充值","getMonthRechargeFlag","setMonthRechargeFlag"),
    LOCK_CAR("lockCar","锁车/解锁","getLockCarFlag","setLockCarFlag"),
    SEARCH_CAR("searchCar","寻车","getSearchCarFlag","setSearchCarFlag"),
    CAR_NUM("carNum","在场车数量","getCurrentInfoType","setCurrentInfoType"),
    FREE_PLACE("freePlace","空余车位数量","getCurrentInfoType","setCurrentInfoType"),
    MONTH_CARD_APPLY("monthCardApply","在线月卡申请","getFlowMode","setFlowMode"),
	USER_NOTICE("userNotice","用户须知","getNoticeFlag","setNoticeFlag"),
	INVOICE_APPLY("invoiceApply","发票申请","getInvoiceFlag","setInvoiceFlag");
	
    private String code;
    private String desc;
    private String getter;
    private String setter;

    ParkingBusinessType(String code, String desc, String getter, String setter) {
        this.code = code;
        this.desc = desc;
        this.getter = getter;
        this.setter = setter;
    }

    public static ParkingBusinessType fromCode(String code) {
        for (ParkingBusinessType bussinessType : ParkingBusinessType.values()) {
            if (bussinessType.code.equals(code) ) {
                return bussinessType;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getGetter() {
        return getter;
    }

    public String getSetter() {
        return setter;
    }
}
