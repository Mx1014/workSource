package com.everhomes.rest.payment;

/**
 * <ul>
 * <li>RECHARGE: 充值</li>
 * <li>CONSUME: 消费</li>
 * </ul>
 */
public enum CardTransactionTypeStatus {
	RECHARGE("1"), CONSUME("2");
    
    private String code;
    private CardTransactionTypeStatus(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static CardTransactionTypeStatus fromCode(String code) {
        if(code != null) {
        	CardTransactionTypeStatus[] values = CardTransactionTypeStatus.values();
            for(CardTransactionTypeStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
