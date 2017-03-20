package com.everhomes.rest.rentalv2;

/***
 * AGREE("agree") 待审批
 * PAID("paid") 待支付节点 
 * COMPLETE("complete") 已完成节点 
 * */
public enum RentalFlowNodeParams {
	
	AGREE("agree"), PAID("paid"),COMPLETE("complete") ;
    
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
