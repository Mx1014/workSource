// @formatter:off
package com.everhomes.rest.enterprise;

public interface EnterpriseServiceErrorCode {
    static final String SCOPE = "enterprise";

    static final int ERROR_ENTERPRISE_NOT_FOUND = 10001;
    static final int ERROR_ENTERPRISE_USER_NOT_FOUND = 10002;

    static final int ERROR_ENTERPRISE_CONTACT_NOT_FOUND = 10101;
    static final int ERROR_ENTERPRISE_CONTACT_PHONENUM_USED = 10111;
    
    static final int ERROR_ENTERPRISE_CONTACT_GROUP_HAS_CHILD = 11101;
}
