// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p>缴费来源</p>
 * <ul>
 * <li>APP("app"): APP支付</li>
 * <li>QRCODE("qrcode"): 扫码支付</li>
 * </ul>
 */
public enum ParkingPaySourceType {
    APP("app","APP支付"),QRCODE("qrcode","扫码支付"),PUBLICACCOUNT("publicaccount","微信公众号支付");

    private String code;
    private String desc;

    private ParkingPaySourceType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return desc;
    }

    public static ParkingPaySourceType fromCode(String code) {
        if(code != null) {
            ParkingPaySourceType[] values = ParkingPaySourceType.values();
            for(ParkingPaySourceType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
