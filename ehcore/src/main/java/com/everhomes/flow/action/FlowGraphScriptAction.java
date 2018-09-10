package com.everhomes.flow.action;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;
import com.everhomes.flow.nashornfunc.NashornScriptMain;
import com.everhomes.rest.flow.FlowScriptType;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.scriptengine.nashorn.NashornEngineService;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class FlowGraphScriptAction extends FlowGraphAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowGraphScriptAction.class);

    transient private FlowService flowService;
    transient private FlowScriptProvider flowScriptProvider;
    transient private NashornEngineService nashornEngineService;
    transient private FlowFunctionService flowFunctionService;

    public FlowGraphScriptAction() {
        flowService = PlatformContext.getComponent(FlowService.class);
        flowScriptProvider = PlatformContext.getComponent(FlowScriptProvider.class);
        nashornEngineService = PlatformContext.getComponent(NashornEngineService.class);
        flowFunctionService = PlatformContext.getComponent(FlowFunctionService.class);
    }

    @Override
    public void fireAction(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException {
        FlowAction flowAction = this.getFlowAction();
        FlowScriptType scriptType = FlowScriptType.fromCode(flowAction.getScriptType());
        if (scriptType == null) {
            return;
        }
        switch (scriptType) {
            case JAVA:
                try {
                    flowFunctionService.invoke(ctx, flowAction.getScriptMainId(), flowAction);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw RuntimeErrorException.errorWith(e, FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_FUNCTION_INVOKE_ERROR,
                            "flow function invoke error, functionId = %s", flowAction.getScriptMainId());
                }
                break;
            case JAVASCRIPT:
                FlowScript script = flowScriptProvider.findByMainIdAndVersion(flowAction.getScriptMainId(), flowAction.getScriptVersion());
                if (script != null) {
                    nashornEngineService.push(new NashornScriptMain(ctx, flowService.toRuntimeScript(script), flowAction));
                } else {
                    LOGGER.warn("can not found script by scriptId = {}, action = {}",
                            flowAction.getScriptMainId(), flowAction);
                }
                break;
            default:
                break;
        }
    }
}