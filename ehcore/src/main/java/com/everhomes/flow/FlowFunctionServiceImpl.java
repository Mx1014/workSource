package com.everhomes.flow;

import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowScriptDTO;
import com.everhomes.rest.flow.FlowScriptInfo;
import com.everhomes.rest.flow.FlowScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlowFunctionServiceImpl implements FlowFunctionService, ApplicationListener<ContextRefreshedEvent> {

    @Autowired(required = false)
    private List<FlowFunctionListener> functionListeners;

    private Map<Long, FlowFunctionInfo> moduleIdToFunctionMap = new HashMap<>();

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private FlowScriptConfigProvider flowScriptConfigProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null || functionListeners == null) {
            return;
        }
        for (FlowFunctionListener listener : functionListeners) {
            Method[] methods = listener.getClass().getDeclaredMethods();

            Set<Method> methodSet = Stream.of(methods)
                    .peek(r -> r.setAccessible(true))
                    .filter(r -> r.getAnnotation(ExportFunction.class) != null)
                    .collect(Collectors.toSet());

            Map<Long, Method> methodMap = new HashMap<>();
            for (Method method : methodSet) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 2) {
                    throw new FlowFunctionException("flow function parameter length invalid " + method);
                }
                if (parameterTypes[0] != FlowCaseState.class) {
                    throw new FlowFunctionException("flow function parameter 0 should be FlowCaseState " + method);
                }
                if (parameterTypes[1] != Map.class) {
                    throw new FlowFunctionException("flow function parameter 1 should be Map " + method);
                }
                ExportFunction annotation = method.getAnnotation(ExportFunction.class);
                if (methodMap.get(annotation.id()) != null) {
                    throw new FlowFunctionException("flow function id duplicate " + method);
                }
                methodMap.put(annotation.id(), method);
            }

            FlowModuleInfo moduleInfo = listener.initModule();
            Long moduleId = moduleInfo.getModuleId();

            // 检查已经使用的函数是否还在
            List<FlowScriptConfig> scriptConfigs = flowScriptConfigProvider.listByModule(moduleId, FlowScriptType.JAVA);
            Set<Long> functionIdSet = scriptConfigs.stream().map(FlowScriptConfig::getScriptMainId).collect(Collectors.toSet());
            for (Long funcId : functionIdSet) {
                if (methodMap.get(funcId) == null) {
                    throw new FlowFunctionException(
                            String.format("configured flow function disappeared, moduleId = %s, functionId = %s", moduleId, funcId));
                }
            }

            FlowFunctionInfo functionInfo = new FlowFunctionInfo();
            functionInfo.setListener(listener);
            functionInfo.setMethodMap(methodMap);
            functionInfo.setModuleInfo(moduleInfo);
            moduleIdToFunctionMap.put(moduleId, functionInfo);
        }
    }

    @Override
    public void invoke(FlowCaseState ctx, Long functionId, FlowAction flowAction) throws InvocationTargetException, IllegalAccessException {
        FlowFunctionInfo functionInfo = moduleIdToFunctionMap.get(ctx.getModuleId());
        if (functionInfo != null) {
            Method method = functionInfo.getMethodMap().get(functionId);
            if (method != null) {
                List<FlowScriptConfig> configList = flowScriptConfigProvider
                        .listByOwner(FlowEntityType.FLOW_ACTION.getCode(), flowAction.getId());

                Map<String, String> config = configList.stream()
                        .collect(Collectors.toMap(FlowScriptConfig::getFieldName, FlowScriptConfig::getFieldValue));
                method.invoke(functionInfo.getListener(), ctx, config);
            }
        }
    }

    @Override
    public List<FlowScriptDTO> listFlowFunctions(Long moduleId, String keyword) {
        FlowFunctionInfo functionInfo = moduleIdToFunctionMap.get(moduleId);
        if (functionInfo != null) {
            return functionInfo.toFlowScriptDTOList(Locale.SIMPLIFIED_CHINESE, localeStringService);
        }
        return new ArrayList<>();
    }

    @Override
    public void validateJavaConfigs(Long moduleId, FlowScriptInfo cmd) {
        FlowFunctionInfo functionInfo = moduleIdToFunctionMap.get(moduleId);
        functionInfo.validateParams(cmd.getId(), cmd.getConfigs());
    }

    @Override
    public Method getExportFlowFunction(Long moduleId, Long functionId) {
        FlowFunctionInfo functionInfo = moduleIdToFunctionMap.get(moduleId);
        if (functionInfo != null) {
            return functionInfo.getFunctionMethod(functionId);
        }
        return null;
    }
}