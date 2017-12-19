package com.everhomes.rest.community;

public interface BuildingServiceErrorCode {

	static final String SCOPE = "building";
    
    static final int ERROR_BUILDING_DELETED = 10001;
    static final int ERROR_BUILDING_NOT_FOUND = 10002;
    static final int ERROR_BUILDING_HAS_CONTRACT = 10003;

}
