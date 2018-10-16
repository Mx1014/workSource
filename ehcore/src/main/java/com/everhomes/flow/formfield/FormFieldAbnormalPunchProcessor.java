package com.everhomes.flow.formfield;

import com.alibaba.fastjson.JSON;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowConditionVariable;
import com.everhomes.flow.FormFieldProcessor;
import com.everhomes.flow.conditionvariable.FlowConditionStringVariable;
import com.everhomes.rest.flow.FlowConditionRelationalOperatorType;
import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.enterpriseApproval.ComponentAbnormalPunchValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by R on 2017/11/16.
 */
@Component
public class FormFieldAbnormalPunchProcessor implements FormFieldProcessor {
    @Override
    public GeneralFormFieldType getSupportFieldType() {
        return GeneralFormFieldType.ABNORMAL_PUNCH;
    }

    @Override
    public List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO) {
        List<FlowConditionVariableDTO> dtoList = new ArrayList<>();
        FlowConditionVariableDTO dto;

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.MULTI_LINE_TEXT.getCode());
        dto.setDisplayName("异常日期");
        dto.setValue("异常日期");
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.MULTI_LINE_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.MULTI_LINE_TEXT.getCode());
        dto.setDisplayName("异常班次");
        dto.setValue("异常班次");
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.MULTI_LINE_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        return dtoList;
    }

    @Override
    public FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO, String variable, String extra) {
        ComponentAbnormalPunchValue abnormalPunch = JSON.parseObject(fieldDTO.getFieldValue(), ComponentAbnormalPunchValue.class);
        if ("异常日期".equals(variable)) {
            return new FlowConditionStringVariable(abnormalPunch.getAbnormalDate());
        } else if ("异常班次".equals(variable)) {
            return new FlowConditionStringVariable(abnormalPunch.getAbnormalItem());
        }
        return null;
    }

    @Override
    public String parseFieldName(Flow flow, String fieldName, String extra) {
        if ("异常日期".equals(fieldName))
            return "异常时间";
        if ("异常班次".equals(fieldName))
            return "异常时间";
        return fieldName;
    }
}
