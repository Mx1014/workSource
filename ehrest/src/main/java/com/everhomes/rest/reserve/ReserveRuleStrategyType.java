// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <ul>
 * <li>REFUND(1): 退款</li>
 * <li>OVERTIME(2): 超时加收</li>
 * </ul>
 */
public enum ReserveRuleStrategyType {
	REFUND((byte)1), OVERTIME((byte)2);

    private byte code;

    private ReserveRuleStrategyType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ReserveRuleStrategyType fromCode(Byte code) {
        if(code != null) {
            ReserveRuleStrategyType[] values = ReserveRuleStrategyType.values();
            for(ReserveRuleStrategyType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
