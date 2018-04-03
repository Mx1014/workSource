//@formatter:off
package com.everhomes.rest.order;

/**
 * <p>会员状态</p>
 *<ul>
 *  <li>INACTIVE(0): 无效</li>
 *  <li>WAITING_FOR_APPROVAL(1): 待审核</li>
 *  <li>ACTIVE(2): 正常</li>
 *</ul>
 */
public enum PaymentUserStatus {
    INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), ACTIVE((byte)2);
    
    private byte code;

    PaymentUserStatus(byte code){
        this.code = code;
    }
    
    public byte getCode() {
        return code;
    }
    
    public static PaymentUserStatus fromCode(Byte code) {
        if(code != null) {
            for (PaymentUserStatus status : PaymentUserStatus.values()) {
                if (status.code == code.byteValue()) {
                    return status;
                }
            }
        }
        
        return null;
    }
}
