package com.everhomes.flow;

import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.flow.FlowScriptConfigDTO;
import com.everhomes.rest.flow.FlowScriptConfigInfo;
import com.everhomes.rest.flow.FlowScriptDTO;
import com.everhomes.rest.flow.FlowScriptType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class FlowFunctionInfo {

    private Map<Long, Method> methodMap;
    private FlowModuleInfo moduleInfo;
    private FlowFunctionListener listener;

    public List<FlowScriptDTO> toFlowScriptDTOList(Locale locale, LocaleStringService localeStringService) {
        List<FlowScriptDTO> dtoList = new ArrayList<>();
        methodMap.forEach((k, v) -> {
            FlowScriptDTO dto = new FlowScriptDTO();
            ExportFunction annotation = v.getAnnotation(ExportFunction.class);
            dto.setDescription(getLocaleText(annotation.desc(), locale, localeStringService));
            dto.setName(v.getName());
            dto.setId((long)k);
            dto.setScriptVersion(annotation.version());
            dto.setScriptType(FlowScriptType.JAVA.getCode());
            dto.setModuleId(moduleInfo.getModuleId());

            dto.setConfigs(toFlowScriptConfigDTOList(annotation, locale, localeStringService));
            dtoList.add(dto);
        });
        return dtoList;
    }

    private List<FlowScriptConfigDTO> toFlowScriptConfigDTOList(ExportFunction annotation, Locale locale, LocaleStringService localeStringService) {
        List<FlowScriptConfigDTO> dtoList = new ArrayList<>();
        for (FuncParam param : annotation.params()) {
            FlowScriptConfigDTO dto = new FlowScriptConfigDTO();
            dto.setFieldName(param.name());
            dto.setFieldDesc(getLocaleText(param.desc(), locale, localeStringService));
            dto.setFieldValue(getLocaleText(param.defaultValue(), locale, localeStringService));
            dtoList.add(dto);
        }
        return dtoList;
    }

    private String getLocaleText(LocaleText localeText, Locale locale, LocaleStringService localeStringService) {
        if (hasLength(localeText.scope()) && hasLength(localeText.code())) {
            return localeStringService.getLocalizedString(
                    localeText.scope(), localeText.code(), locale.toString(), localeText.value());
        }
        return localeText.value();
    }

    private boolean hasLength(String string) {
        return string != null && string.trim().length() > 0;
    }

    public Map<Long, Method> getMethodMap() {
        return methodMap;
    }

    public void setMethodMap(Map<Long, Method> methodMap) {
        this.methodMap = methodMap;
    }

    public FlowFunctionListener getListener() {
        return listener;
    }

    public void setListener(FlowFunctionListener listener) {
        this.listener = listener;
    }

    public FlowModuleInfo getModuleInfo() {
        return moduleInfo;
    }

    public void setModuleInfo(FlowModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public void validateParams(Long methodId, List<FlowScriptConfigInfo> configs) {
        Method method = methodMap.get(methodId);

        Map<String, String> kvMap = configs.stream().peek(r -> {
            if (r.getFieldValue() == null) {
                r.setFieldValue("");
            }
        }).collect(Collectors.toMap(FlowScriptConfigInfo::getFieldName, FlowScriptConfigInfo::getFieldValue));

        if (method != null) {
            ExportFunction annotation = method.getAnnotation(ExportFunction.class);
            for (FuncParam param : annotation.params()) {
                try {
                    param.validator().newInstance().validate(param, kvMap.get(param.name()));
                } catch (InstantiationException | IllegalAccessException e) {
                   throw new RuntimeException(e);
                }
            }
        }
    }

    public Method getFunctionMethod(Long methodId) {
        return methodMap.get(methodId);
    }
}