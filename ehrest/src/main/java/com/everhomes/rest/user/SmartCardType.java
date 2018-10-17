package com.everhomes.rest.user;

/**
 * <ul>
 * <li>SMART_CARD_PAY: 支付一卡通</li>
 * <li>SMART_CARD_ACLINK: 门禁一卡通</li>
 * </ul>
 * @author janson
 *
 */
public enum SmartCardType {
	SMART_CARD_PAY((byte) 1),  SMART_CARD_ACLINK((byte) 2);
	
	 private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private SmartCardType(byte code) {
        this.code = code;
    }
    
    public static SmartCardType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return SMART_CARD_PAY;
        case 1 :
            return SMART_CARD_ACLINK;
        }
        
        return null;
    }
}
