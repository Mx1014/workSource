// @formatter:off
package com.everhomes.rest.address;

public interface AddressServiceErrorCode {
    String SCOPE = "address";
    int ERROR_ADDRESS_NOT_EXIST = 10001;
    
    
    int ERROR_BUILDING_NAME_EMPTY = 20001;
    int ERROR_APARTMENT_NAME_EMPTY = 20002;
    int ERROR_AREA_SIZE_NOT_NUMBER = 20003;
    int ERROR_EXISTS_APARTMENT_NAME = 20004;
    
    
}
