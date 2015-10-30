package com.everhomes.enterprise;

public interface EnterpriseNotifyTemplateCode {
    static final String SCOPE = "enterprise.notification";
    static final int ENTERPRISE_USER_SUCCESS_MYSELF = 1;   //The user success join company
    static final int ENTERPRISE_USER_SUCCESS_OTHER = 2; //broadcast joining info to members' of company
    static final int ENTERPRISE_USER_REJECT_JOIN = 3; //reject for joining to enterprise
}
