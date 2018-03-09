package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li>SOCIALSECURITYUNPAY(1): 社保在缴</li>
 * <li>ACCUMULATIONFUND_UNPAY(2): 公积金在缴</li>
 * <li>INCREASE(3): 本月增员</li>
 * <li>DECREASE(4): 本月减员</li>
 * <li>INWORK(5):本月入职</li>
 * <li>OUTWORK(6): 本月离职</li>
 * </ul>
 */
public enum SocialSecurityItem {

    SOCIALSECURITYUNPAY((byte)1,"社保未缴"),
    ACCUMULATIONFUND_UNPAY((byte)2,"公积金未缴"),
    INCREASE((byte)3,"本月增员"),
    DECREASE((byte)4,"本月减员"),
    INWORK((byte)5,"本月入职"),
    OUTWORK((byte)6, "本月离职");

    private byte code;
	private String describe;
    private SocialSecurityItem(byte code, String describe) {
        this.code = code;
        this.describe = describe;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static SocialSecurityItem fromCode(Byte code) {
        for(SocialSecurityItem t : SocialSecurityItem.values()) {
            if (code !=null && t.code == code.byteValue()) {
                return t;
            }
        }
        
        return null;
    }

	public String getDescribe() {
		return describe;
	}
 
}
