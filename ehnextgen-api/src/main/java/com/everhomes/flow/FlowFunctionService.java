package com.everhomes.flow;

import com.everhomes.rest.flow.FlowScriptInfo;
import com.everhomes.rest.flow.FlowScriptDTO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public interface FlowFunctionService {

    void invoke(FlowCaseState ctx, Long functionId, FlowAction flowAction) throws InvocationTargetException, IllegalAccessException;

    List<FlowScriptDTO> listFlowFunctions(Long moduleId, String keyword);

    void validateJavaConfigs(Long moduleId, FlowScriptInfo cmd);

    Method getExportFlowFunction(Long moduleId, Long functionId);
}
