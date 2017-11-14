package com.everhomes.flow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FormFieldProcessorManagerImpl implements FormFieldProcessorManager, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormFieldProcessorManagerImpl.class);

    @Autowired(required = false)
    private List<FormFieldProcessor> processors;

    private Map<GeneralFormFieldType, FormFieldProcessor> processorMap;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (processors != null) {
            processorMap = new HashMap<>();
            for (FormFieldProcessor processor : processors) {
                try {
                    processorMap.put(processor.getSupportFieldType(), processor);
                } catch (Exception e) {
                    LOGGER.error("General form file processor init error, fieldType = "
                            + processor.getSupportFieldType() + ", processor = " + processor, e);
                }
            }
        }
    }

    @Override
    public List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO) {
        GeneralFormFieldType fieldType = GeneralFormFieldType.fromCode(fieldDTO.getFieldType());
        FormFieldProcessor fieldProcessor = processorMap.get(fieldType);
        if (fieldProcessor != null) {
            return fieldProcessor.convertFieldDtoToFlowConditionVariableDto(flow, fieldDTO);
        }
        return null;
    }

    @Override
    public FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO) {
        GeneralFormFieldType fieldType = GeneralFormFieldType.fromCode(fieldDTO.getFieldType());
        FormFieldProcessor fieldProcessor = processorMap.get(fieldType);
        if (fieldProcessor != null) {
            try {
                return fieldProcessor.getFlowConditionVariable(fieldDTO);
            } catch (Exception e) {
                throw RuntimeErrorException.errorWith(e, FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CONDITION_VARIABLE,
                        "Flow condition variable parse error, fieldDTO=%s", fieldDTO);
            }
        } else {
            LOGGER.warn("Not found processor of fieldType {}", fieldDTO.getFieldType());
        }
        return null;
    }

    @Override
    public String parseFormFieldName(FlowCaseState ctx, String variable, String extra) {
        Object fieldTypeExtra = null;
        try {
            JSONObject extraObject = JSON.parseObject(extra);
            fieldTypeExtra = extraObject.get("fieldType");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fieldTypeExtra == null) {
            return variable;
        }
        GeneralFormFieldType fieldType = GeneralFormFieldType.fromCode(fieldTypeExtra.toString());

        FormFieldProcessor fieldProcessor = processorMap.get(fieldType);
        if (fieldProcessor != null) {
            try {
                return fieldProcessor.parseFormFieldName(ctx, variable, extra);
            } catch (Exception e) {
                throw RuntimeErrorException.errorWith(e, FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CONDITION_VARIABLE,
                        "Flow condition variable parse error, fieldType=%s, extra=%s", fieldType, extra);
            }
        } else {
            LOGGER.warn("Not found processor of fieldType {}", fieldTypeExtra);
        }
        return null;
    }
}
