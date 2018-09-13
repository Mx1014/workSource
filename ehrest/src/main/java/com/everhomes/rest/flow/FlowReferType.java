package com.everhomes.rest.flow;

public enum FlowReferType {

	RENTAL( "rental.order"),APPROVAL("approval"),PUNCH_APPROVAL("punch_approval"),COMMUNITY_APPROVE("community_approve"),
	OFFICE_CUBICLE("office_cubicle"),
	EXPRESS("express"),SERVICE_ALLIANCE("service_alliance");

	private String code;
    private FlowReferType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowReferType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}

    	for(FlowReferType t : FlowReferType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
