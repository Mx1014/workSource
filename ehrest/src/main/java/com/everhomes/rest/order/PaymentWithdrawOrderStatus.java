//@formatter:off
package com.everhomes.rest.order;

/**
 * <p>提现订单状态</p>
 *<ul>
 *  <li>0: 无效</li>
 *  <li>1: 已下单，等待提现结果</li>
 *  <li>2: 提现成功</li>
 *  <li>3: 提现失败</li>
 *</ul>
 */
public enum PaymentWithdrawOrderStatus {
    INACTIVE((byte)0), WAITING_FOR_CONFIRM((byte)1), SUCCESS((byte)2), FAILED((byte)3);
    
    private byte code;

    PaymentWithdrawOrderStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static PaymentWithdrawOrderStatus fromCode(Byte code) {
        if(code != null) {
            for (PaymentWithdrawOrderStatus status : PaymentWithdrawOrderStatus.values()) {
                if (status.code == code.byteValue()) {
                    return status;
                }
            }
        }
        
        return null;
    }
}
