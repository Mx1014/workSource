package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>ENTERPRISE("ENTERPRISE"): 属于一家企业</li>
 * <li>DEPARTMENT("DEPARTMENT"): 属于一个部门</li>
 * <li>COMMUNITY("COMMUNITY"): 属于一个园区</li>
 * <li>PARKING("PARKING"): PARKING</li>
 * <li>RENTALRESOURCETYPE("RENTALRESOURCETYPE"): RENTALRESOURCETYPE</li>
 * <li>PMTASK("PMTASK"): PMTASK</li>
 * <li>GENERAL_APPROVAL("GENERAL_APPROVAL"): GENERAL_APPROVAL</li>
 * <li>RESERVER_PLACE("RESERVER_PLACE"): RESERVER_PLACE</li>
 * <li>EhOrganizations("EhOrganizations"): EhOrganizations</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowOwnerType {
	ENTERPRISE("ENTERPRISE"),
    DEPARTMENT("DEPARTMENT"),
    COMMUNITY("COMMUNITY"),
    PARKING("PARKING"),
	RENTALRESOURCETYPE("RENTALRESOURCETYPE"),
    PMTASK("PMTASK"),
    GENERAL_APPROVAL("GENERAL_APPROVAL"),
    COMMUNITY_APPROVE("COMMUNITY_APPROVE"),
	RESERVER_PLACE("RESERVER_PLACE"), 
	EhOrganizations("EhOrganizations"), 
    WAREHOUSE_REQUEST("WAREHOUSEREQUEST"),
    LEASE_PROJECT("LEASE_PROJECT"),
    LEASE_PROMOTION("LEASE_PROMOTION"),
    LEASE_RENEW("LEASE_RENEW"),
    TALENT_REQUEST("TALENT_REQUEST"),
    CONTRACT("CONTRACT"),
    PAYMENT_CONTRACT("PAYMENT_CONTRACT"),
    CUSTOM_REQUEST("CUSTOM_REQUEST"),
    PARKING_CAR_VERIFICATION("PARKING_CAR_VERIFICATION"),
    RELOCATION_REQUEST("EhRelocationRequests"),
    PAYMENT_APPLICATION("PAYMENT_APPLICATION"),
    PURCHASE_REQUISITION("PURCHASE_REQUISITION"),
    REQUEST_REQUISITION("REQUEST_REQUISITION"),
    PAYMENT_REQUISITION("PAYMENT_REQUISITION"),
    CONTRACT_REQUISITION("CONTRACT_REQUISITION"),
    PURCHASE("PURCHASE"),
    SERVICE_ALLIANCE("SERVICE_ALLIANCE")
    ;

	private String code;
    private FlowOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowOwnerType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowOwnerType t : FlowOwnerType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
