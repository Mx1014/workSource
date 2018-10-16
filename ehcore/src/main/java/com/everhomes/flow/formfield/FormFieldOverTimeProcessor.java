package com.everhomes.flow.formfield;

import com.alibaba.fastjson.JSON;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowConditionVariable;
import com.everhomes.flow.FormFieldProcessor;
import com.everhomes.flow.conditionvariable.FlowConditionStringVariable;
import com.everhomes.rest.enterpriseApproval.ComponentOverTimeValue;
import com.everhomes.rest.flow.FlowConditionRelationalOperatorType;
import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.techpark.punch.utils.PunchDayParseUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by R on 2017/11/16.
 */
@Component
public class FormFieldOverTimeProcessor implements FormFieldProcessor {
    @Override
    public GeneralFormFieldType getSupportFieldType() {
        return GeneralFormFieldType.OVERTIME;
    }

    @Override
    public List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO) {
        List<FlowConditionVariableDTO> dtoList = new ArrayList<>();
        FlowConditionVariableDTO dto;

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.MULTI_LINE_TEXT.getCode());
        dto.setDisplayName("开始时间");
        dto.setValue("开始时间");
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.MULTI_LINE_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.MULTI_LINE_TEXT.getCode());
        dto.setDisplayName("结束时间");
        dto.setValue("结束时间");
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.MULTI_LINE_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.NUMBER_TEXT.getCode());
        dto.setDisplayName("加班时长");
        dto.setValue("加班时长");
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.NUMBER_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        return dtoList;
    }

    @Override
    public FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO, String variable, String extra) {

        ComponentOverTimeValue overTime = JSON.parseObject(fieldDTO.getFieldValue(), ComponentOverTimeValue.class);
        if ("开始时间".equals(variable)) {
            return new FlowConditionStringVariable(overTime.getStartTime());
        } else if ("结束时间".equals(variable)) {
            return new FlowConditionStringVariable(overTime.getEndTime());
        } else if ("加班时长".equals(variable)) {
            if (overTime.getDurationInMinute() != null) {
                return new FlowConditionStringVariable(PunchDayParseUtils.parseHourMinuteDisplayStringZeroWithUnit(overTime.getDurationInMinute() * 60 * 1000, "小时", "分钟"));
            } else {
                return new FlowConditionStringVariable(overTime.getDuration() + "天");
            }
        }
        return null;
    }

    @Override
    public String parseFieldName(Flow flow, String fieldName, String extra) {
        if ("开始时间".equals(fieldName))
            return "加班时段";
        if ("结束时间".equals(fieldName))
            return "加班时段";
        if ("加班时长".equals(fieldName))
            return "加班时段";
        return fieldName;
    }
}
