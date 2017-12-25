package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li>SOCIALSECURITYPAY(1): 社保在缴</li>
 * <li>ACCUMULATIONFUNDPAY(2): 公积金在缴</li>
 * <li>BOTHPAY(3): 社保和公积金在缴</li>
 * </ul>
 */
public enum PayItem {

    YILIAO("养老"),
    YANGLAO("医疗"),
    SHENGYU("生育"),
    SHIYE("失业"),
    GONGSHANG("工伤");

    private String code;

    private PayItem(String code ) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PayItem fromCode(String code) {
        for(PayItem t : PayItem.values()) {
            if (code != null && code.equals(t.code)) {
                return t;
            }
        }
        return null;
    }


}
