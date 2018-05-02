package com.everhomes.flow.action;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;
import com.everhomes.flow.nashornfunc.NashornScriptMain;
import com.everhomes.rest.flow.FlowScriptType;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class FlowGraphScriptAction extends FlowGraphAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowGraphScriptAction.class);

    transient private FlowScriptProvider flowScriptProvider;
    transient private FlowNashornEngineService flowNashornEngineService;
    transient private FlowFunctionService flowFunctionService;

    public FlowGraphScriptAction() {
        flowScriptProvider = PlatformContext.getComponent(FlowScriptProvider.class);
        flowNashornEngineService = PlatformContext.getComponent(FlowNashornEngineService.class);
        flowFunctionService = PlatformContext.getComponent(FlowFunctionService.class);
    }

    @Override
    public void fireAction(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException {
        FlowScriptType scriptType = FlowScriptType.fromCode(this.getFlowAction().getScriptType());

        switch (scriptType) {
            case JAVA:
                try {
                    flowFunctionService.invoke(ctx, Integer.valueOf(this.getFlowAction().getScriptId()+""));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw RuntimeErrorException.errorWith(e, FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_FUNCTION_INVOKE_ERROR,
                            "flow function invoke error, functionId = %s", this.getFlowAction().getScriptId());
                }
                break;
            case JAVASCRIPT:
                FlowScript flowScript = flowScriptProvider.findById(this.getFlowAction().getScriptId());
                if (flowScript != null) {
                    flowNashornEngineService.push(new NashornScriptMain(ctx, flowScript));
                } else {
                    LOGGER.warn("can not found script by scriptId = {}, action = {}",
                            this.getFlowAction().getScriptId(), getFlowAction());
                }
                break;
            default:
                break;
        }
    }
}