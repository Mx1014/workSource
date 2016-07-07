package com.everhomes.rest.payment;

/**
 * <ul>
 * <li>ALIPAY: 支付宝</li>
 * <li>WECHATPAY: 微信</li>
 * </ul>
 */
public enum PaidTypeStatus {
	ALIPAY("10001"), WECHATPAY("10002");
    
    private String code;
    private PaidTypeStatus(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PaidTypeStatus fromCode(String code) {
        if(code != null) {
        	PaidTypeStatus[] values = PaidTypeStatus.values();
            for(PaidTypeStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
