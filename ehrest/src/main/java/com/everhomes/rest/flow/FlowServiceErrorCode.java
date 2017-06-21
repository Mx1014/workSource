package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>ERROR_FLOW_REMIND_ERROR: 催办错误</li>
 * </ul>
 * @author janson
 *
 */
public interface FlowServiceErrorCode {
    static final String SCOPE = "flow";
    
    static final int ERROR_FLOW_NAME_EXISTS = 10001;
    static final int ERROR_FLOW_NOT_EXISTS = 10002;
    static final int ERROR_FLOW_NODE_LEVEL_ERR = 10003;
    static final int ERROR_FLOW_CONFIG_BUSY = 10004;
    static final int ERROR_FLOW_SNAPSHOT_NOEXISTS = 10005;
    static final int ERROR_FLOW_CASE_NOEXISTS = 10006;
    static final int ERROR_FLOW_NODE_NOEXISTS = 10007;
    static final int ERROR_FLOW_STEP_ERROR = 10008;
    static final int ERROR_FLOW_PARAM_ERROR = 10009;
    static final int ERROR_FLOW_REMIND_ERROR = 10010;
    static final int ERROR_FLOW_EVALUATE_ITEM_SIZE_ERROR = 10011;
}
