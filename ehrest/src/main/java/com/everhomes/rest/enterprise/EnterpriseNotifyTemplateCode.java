package com.everhomes.rest.enterprise;

public interface EnterpriseNotifyTemplateCode {
    static final String SCOPE = "enterprise.notification";
    static final int ENTERPRISE_USER_SUCCESS_MYSELF = 1;   //The user success join company
    static final int ENTERPRISE_USER_SUCCESS_OTHER = 2; //broadcast joining info to members' of company
    static final int ENTERPRISE_USER_REJECT_JOIN = 3; //reject for joining to enterprise
    static final int ENTERPRISE_CONTACT_LEAVE_FOR_APPLICANT = 4; // enterprise contact leave for applicant
    static final int ENTERPRISE_CONTACT_LEAVE_FOR_OTHER = 5; // enterprise contact leave for others
    static final int ENTERPRISE_CONTACT_REQUEST_TO_JOIN_FOR_APPLICANT = 6; // enterprise contact request to join the enterprise (for applicant)
    static final int ENTERPRISE_CONTACT_REQUEST_TO_JOIN_FOR_OPERATOR = 7; // enterprise contact request to join the enterprise (for operator)
}
