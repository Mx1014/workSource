//@formatter:off
package com.everhomes.rest.order;

/**
 * <p>订单是否提交标记</p>
 *<ul>
 *  <li>NO(0): 不提交</li>
 *  <li>YES(1): 提交</li>
 *</ul>
 */
public enum PaymentCommitFlag {
    NO((byte)0), YES((byte)1);
    
    private int code;

    PaymentCommitFlag(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PaymentCommitFlag fromCode(Integer code) {
        if(code != null) {
            for (PaymentCommitFlag status : PaymentCommitFlag.values()) {
                if (status.code == code.intValue()) {
                    return status;
                }
            }
        }
        
        return null;
    }
}
