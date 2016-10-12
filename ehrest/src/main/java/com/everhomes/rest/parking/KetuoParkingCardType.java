// @formatter:off
package com.everhomes.rest.parking;

/**
 * <p></p>
 * <ul>
 * <li>MONTHLY("1", "月租车")</li>
 * </ul>
 */
public enum KetuoParkingCardType {
    MONTHLY("1", "月租车");
    
    private String code;
    private String text;
    
    private KetuoParkingCardType(String code, String text) {
        this.code = code;
        this.text = text;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static KetuoParkingCardType fromCode(String code) {
        if(code != null) {
            KetuoParkingCardType[] values = KetuoParkingCardType.values();
            for(KetuoParkingCardType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }

	public String getText() {
		return text;
	}

}
