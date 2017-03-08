package com.everhomes.rest.rentalv2;

/***
 * AGREE("agree") 同意节点
 * PAID("paid") 已支付节点 
 * */
public enum RentalFlowNodeParams {
	
	AGREE("agree"), PAID("paid") ;
    
    private String code;
    private RentalFlowNodeParams(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static RentalFlowNodeParams fromType(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(AGREE.getCode())) {
        	return AGREE;
        }

        if(code.equalsIgnoreCase(PAID.getCode())) {
        	return PAID;
        }
        return null;
    }
}
