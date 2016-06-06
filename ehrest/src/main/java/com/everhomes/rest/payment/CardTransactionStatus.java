package com.everhomes.rest.payment;

/**
 * <ul>
 * <li>FAIL(0): 失败</li>
 * <li>EXPIRED(1): 过期</li>
 * <li>REVOKED(2): 已撤销</li>
 * <li>PAIDED(3): 支付成功</li>
 * <li>UNPAIDED(4): 未支付</li>
 * </ul>
 */
public enum CardTransactionStatus {
	FAIL((byte)0), EXPIRED((byte)1),REVOKED((byte)2),PAIDED((byte)3),UNPAIDED((byte)4);
        
    private Byte code;
    private CardTransactionStatus(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static CardTransactionStatus fromCode(Byte code) {
        if(code != null) {
        	CardTransactionStatus[] values = CardTransactionStatus.values();
            for(CardTransactionStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
