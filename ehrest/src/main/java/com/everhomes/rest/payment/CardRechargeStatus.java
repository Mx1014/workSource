// @formatter:off
package com.everhomes.rest.payment;

/**
 * <ul>
 * <li>FAIL(0): 充值失败</li>
 * <li>UNRECHARGED(1): 未充值,处理中</li>
 * <li>RECHARGED(2): 充值成功</li>
 * <li>COMPLETE(3): 处理完成</li>
 * <li>REFUNDING(4): 退款中</li>
 * <li>REFUNDED(5): 已退款</li>
 * </ul>
 */
public enum CardRechargeStatus {
	FAIL((byte)0), UNRECHARGED((byte)1), RECHARGED((byte)2) ,COMPLETE((byte)3),REFUNDING((byte)4),REFUNDED((byte)5);
    
    private byte code;
    
    private CardRechargeStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static CardRechargeStatus fromCode(Byte code) {
        if(code != null) {
            CardRechargeStatus[] values = CardRechargeStatus.values();
            for(CardRechargeStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
