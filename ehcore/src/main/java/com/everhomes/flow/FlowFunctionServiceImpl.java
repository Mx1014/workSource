package com.everhomes.flow;

import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.flow.FlowScriptInfo;
import com.everhomes.rest.flow.FlowScriptConfigDTO;
import com.everhomes.rest.flow.FlowScriptDTO;
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
        if (event.getApplicationContext().getParent() != null) {
            return;
        }
        for (FlowFunctionListener listener : functionListeners) {
            try {
                Method[] methods = listener.getClass().getDeclaredMethods();

                Map<Integer, Method> identifierToMethodMap = Stream.of(methods)
                        .peek(r -> r.setAccessible(true))
                        .filter(r -> r.getAnnotation(ExportFunction.class) != null)
                        .collect(Collectors.toMap(r -> {
                            StringBuilder id = new StringBuilder(r.getName());
                            for (Class<?> paramType : r.getParameterTypes()) {
                                id.append(":").append(paramType.getName());
                            }
                            return id.toString().hashCode();
                        }, r -> r));

                FlowFunctionInfo functionInfo = new FlowFunctionInfo();
                functionInfo.setListener(listener);
                functionInfo.setMethodMap(identifierToMethodMap);

                FlowModuleInfo moduleInfo = listener.initModule();
                functionInfo.setModuleInfo(moduleInfo);

                moduleIdToFunctionMap.put(moduleInfo.getModuleId(), functionInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void invoke(FlowCaseState ctx, Integer functionId) throws InvocationTargetException, IllegalAccessException {
        FlowFunctionInfo functionInfo = moduleIdToFunctionMap.get(ctx.getModuleId());
        if (functionInfo != null) {
            Method method = functionInfo.getMethodMap().get(functionId);
            if (method != null) {
                FlowCase flowCase = ctx.getFlowCase();
                List<FlowScriptConfig> configList = flowScriptConfigProvider
                        .listByFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());

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
        functionInfo.validateParams(Integer.valueOf(cmd.getId()+""), cmd.getConfigs());
    }

    @Override
    public Method getExportFlowFunction(Long moduleId, Long functionId) {
        FlowFunctionInfo functionInfo = moduleIdToFunctionMap.get(moduleId);
        if (functionInfo != null) {
            return functionInfo.getFunctionMethod(Integer.valueOf(functionId+""));
        }
        return null;
    }
}