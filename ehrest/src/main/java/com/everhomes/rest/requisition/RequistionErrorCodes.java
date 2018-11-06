//@formatter:off
package com.everhomes.rest.requisition;

/**
 * Created by Wentian Wang on 2018/2/5.
 */

public interface RequistionErrorCodes {
    static final String SCOPE = "requisition";
    static final Integer ERROR_CREATE_FLOW_CASE = 1001;
    static final Integer ERROR_NO_ONE_APPROVAL = 506;
    static final Integer ERROR_NO_USED_APPROVAL = 507;

    static final Integer ERROR_FLOW_BEING_APPROVAL = 1004;

    static final int ERROR_FORM_NOT_FIND = 1003;

    static final int ERROR_FORM_PARAM = 1002;  //表单ID及版本参数不全
    static final int ERROR_APPROVAL_NOT_START = 2001;
}
