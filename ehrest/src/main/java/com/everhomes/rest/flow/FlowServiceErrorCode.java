package com.everhomes.rest.flow;

public interface FlowServiceErrorCode {
    static final String SCOPE = "flow";
    
    static final int ERROR_FLOW_NAME_EXISTS = 10001;
    static final int ERROR_FLOW_NOT_EXISTS = 10002;
    static final int ERROR_FLOW_NODE_LEVEL_ERR = 10003;
    static final int ERROR_FLOW_CONFIG_BUSY = 10004;
    static final int ERROR_FLOW_SNAPSHOT_NOEXISTS = 10005;
    static final int ERROR_FLOW_CASE_NOEXISTS = 10006;
    static final int ERROR_FLOW_NODE_NOEXISTS = 10006;
}
