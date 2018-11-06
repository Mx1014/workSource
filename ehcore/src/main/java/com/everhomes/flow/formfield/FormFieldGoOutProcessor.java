package com.everhomes.flow.formfield;

import com.alibaba.fastjson.JSON;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowConditionVariable;
import com.everhomes.flow.FormFieldGroupProcessor;
import com.everhomes.flow.FormFieldProcessor;
import com.everhomes.flow.conditionvariable.FlowConditionNumberVariable;
import com.everhomes.flow.conditionvariable.FlowConditionStringVariable;
import com.everhomes.rest.flow.FlowConditionRelationalOperatorType;
import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.enterpriseApproval.ComponentGoOutValue;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by R on 2017/11/16.
 */
@Component
public class FormFieldGoOutProcessor implements FormFieldGroupProcessor {
    @Override
    public GeneralFormFieldType getSupportFieldType() {
        return GeneralFormFieldType.GO_OUT;
    }

    @Override
    public List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO) {
        List<FlowConditionVariableDTO> dtoList = new ArrayList<>();
        FlowConditionVariableDTO dto;
        String extra = StringHelper.toJsonString(fieldDTO);
        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.MULTI_LINE_TEXT.getCode());
        dto.setDisplayName("开始时间");
        dto.setValue("开始时间");
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.MULTI_LINE_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dto.setExtra(extra);
        dtoList.add(dto);

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.MULTI_LINE_TEXT.getCode());
        dto.setDisplayName("结束时间");
        dto.setValue("结束时间");
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.MULTI_LINE_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dto.setExtra(extra);
        dtoList.add(dto);

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.NUMBER_TEXT.getCode());
        dto.setDisplayName("外出时长");
        dto.setValue("外出时长");
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.NUMBER_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dto.setExtra(extra);
        dtoList.add(dto);

        return dtoList;
    }

    @Override
    public FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO, String variable, String extra) {

        ComponentGoOutValue goOut = JSON.parseObject(fieldDTO.getFieldValue(), ComponentGoOutValue.class);
        if ("开始时间".equals(variable)) {
            return new FlowConditionStringVariable(goOut.getStartTime().substring(0,10));
        } else if ("结束时间".equals(variable)) {
            return new FlowConditionStringVariable(goOut.getEndTime().substring(0,10));
        } else if ("外出时长".equals(variable)) {
            return new FlowConditionNumberVariable(goOut.getDuration());
        }
        return null;
    }

    @Override
    public String parseFieldName(Flow flow, String fieldName, String extra) {
        if ("开始时间".equals(fieldName))
            return "外出时段";
        if ("结束时间".equals(fieldName))
            return "外出时段";
        if ("外出时长".equals(fieldName))
            return "外出时段";
        return fieldName;
    }
}
