package com.everhomes.flow.formfield;

import com.alibaba.fastjson.JSON;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowConditionVariable;
import com.everhomes.flow.FormFieldProcessor;
import com.everhomes.flow.conditionvariable.FlowConditionDateVariable;
import com.everhomes.rest.flow.FlowConditionRelationalOperatorType;
import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormAskForLeaveValue;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/11/3.
 */
@Component
public class FormFieldAskForLeaveProcessor implements FormFieldProcessor {

    @Override
    public GeneralFormFieldType getSupportFieldType() {
        return GeneralFormFieldType.ASK_FOR_LEAVE;
    }

    @Override
    public List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO) {
        List<FlowConditionVariableDTO> dtoList = new ArrayList<>();
        FlowConditionVariableDTO dto;

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.DROP_BOX.getCode());
        dto.setDisplayName("请假类型");
        dto.setName("请假类型");
        dto.setOptions(null);
        dto.setExtra("");
        //todo:操作符确定
        // PostApprovalFormAskForLeaveValue field = JSON.parseObject(fieldDTO.getFieldValue(), PostApprovalFormAskForLeaveValue.class);
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.DROP_BOX).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.MULTI_LINE_TEXT.getCode());
        dto.setDisplayName("开始时间");
        dto.setName("开始时间");
        dto.setExtra("");
        //todo:操作符确定
        // PostApprovalFormAskForLeaveValue field = JSON.parseObject(fieldDTO.getFieldValue(), PostApprovalFormAskForLeaveValue.class);
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.MULTI_LINE_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.MULTI_LINE_TEXT.getCode());
        dto.setDisplayName("结束时间");
        dto.setName("结束时间");
        dto.setExtra("");
        //todo:操作符确定
        // PostApprovalFormAskForLeaveValue field = JSON.parseObject(fieldDTO.getFieldValue(), PostApprovalFormAskForLeaveValue.class);
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.MULTI_LINE_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        dto = new FlowConditionVariableDTO();
        dto.setFieldType(GeneralFormFieldType.NUMBER_TEXT.getCode());
        dto.setDisplayName("请假时长");
        dto.setName("请假时长");
        dto.setExtra("");
        //todo:操作符确定,是否支持浮点数
        // PostApprovalFormAskForLeaveValue field = JSON.parseObject(fieldDTO.getFieldValue(), PostApprovalFormAskForLeaveValue.class);
        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.NUMBER_TEXT).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));
        dtoList.add(dto);

        return dtoList;
    }

    @Override
    public FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO, String variable, String extra) {
        return null;
    }

    @Override
    public String parseFieldName(Flow flow, String fieldName, String extra) {
        return null;
    }

}
