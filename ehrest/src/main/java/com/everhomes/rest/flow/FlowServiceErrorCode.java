package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>ERROR_FLOW_REMIND_ERROR: 催办错误</li>
 * </ul>
 * @author janson
 *
 */
public interface FlowServiceErrorCode {
    String SCOPE = "flow";
    
    int ERROR_FLOW_NAME_EXISTS = 10001;
    int ERROR_FLOW_NOT_EXISTS = 10002;
    int ERROR_FLOW_NODE_LEVEL_ERR = 10003;
    int ERROR_FLOW_CONFIG_BUSY = 10004;
    int ERROR_FLOW_SNAPSHOT_NOEXISTS = 10005;
    int ERROR_FLOW_CASE_NOEXISTS = 10006;
    int ERROR_FLOW_NODE_NOEXISTS = 10007;
    int ERROR_FLOW_STEP_ERROR = 10008;
    int ERROR_FLOW_PARAM_ERROR = 10009;
    int ERROR_FLOW_REMIND_ERROR = 10010;
    int ERROR_FLOW_EVALUATE_ITEM_SIZE_ERROR = 10011;

    int ERROR_FLOW_LANE_NOEXISTS = 100012;
}
