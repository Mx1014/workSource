package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li>SOCIALSECURITYUNPAY(1): 社保在缴</li>
 * <li>ACCUMULATIONFUND_UNPAY(2): 公积金在缴</li>
 * <li>BOTHPAY(3): 社保和公积金在缴</li>
 * </ul>
 */
public enum SsorAfPay {

    SOCIALSECURITYPAY((byte)1,"社保在缴"),
    ACCUMULATIONFUNDPAY((byte)2,"公积金在缴"),
    BOTHPAY((byte)3,"社保和公积金在缴");

    private byte code;
	private String describe;
    private SsorAfPay(byte code, String describe) {
        this.code = code;
        this.describe = describe;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static SsorAfPay fromCode(Byte code) {
        for(SsorAfPay t : SsorAfPay.values()) {
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
