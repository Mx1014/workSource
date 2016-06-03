// @formatter:off
package com.everhomes.rest.payment;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>FAIL: 失败</li>
 * <li>SUCCESS: 成功</li>
 * </ul>
 */
public enum PaidResultStatus {
	FAIL((byte)0),SUCCESS((byte)1);
    
    private Byte code;
    private PaidResultStatus(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static PaidResultStatus fromCode(Byte code) {
        if(code != null) {
            PaidResultStatus[] values = PaidResultStatus.values();
            for(PaidResultStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
