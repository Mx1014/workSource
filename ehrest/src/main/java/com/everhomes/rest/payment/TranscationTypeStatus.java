package com.everhomes.rest.payment;

/**
 * <ul>
 * <li>RECHARGE: 充值</li>
 * <li>CONSUME: 消费</li>
 * </ul>
 */
public enum TranscationTypeStatus {
	RECHARGE("0"), CONSUME("1");
    
    private String code;
    private TranscationTypeStatus(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static TranscationTypeStatus fromCode(String code) {
        if(code != null) {
        	TranscationTypeStatus[] values = TranscationTypeStatus.values();
            for(TranscationTypeStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
