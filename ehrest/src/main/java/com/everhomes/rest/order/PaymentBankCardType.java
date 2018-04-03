package com.everhomes.rest.order;

/**
 * <p>提现订单状态</p>
 *<ul>
 *  <li>PERSONAL(0): 个人银行卡号</li>
 *  <li>BUSINESS(1): 企业对公银行卡号</li>
 *</ul>
 */
public enum PaymentBankCardType {
    PERSONAL(0), BUSINESS(1);
    
    private int code;
    private PaymentBankCardType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PaymentBankCardType fromCode(Integer code) {
        if(code != null) {
            for(PaymentBankCardType val: PaymentBankCardType.values()) {
                if(val.getCode() == code.intValue())
                    return val;
            }
        }

        return null;
    }
}
