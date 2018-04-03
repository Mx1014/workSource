package com.everhomes.flow.formfield;

import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowConditionVariable;
import com.everhomes.flow.FormFieldProcessor;
import com.everhomes.flow.conditionvariable.FlowConditionDateVariable;
import com.everhomes.flow.conditionvariable.FlowConditionStringVariable;
import com.everhomes.rest.flow.FlowConditionRelationalOperatorType;
import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/11/3.
 */
@Component
public class FormFieldDateProcessor implements FormFieldProcessor {

    @Override
    public GeneralFormFieldType getSupportFieldType() {
        return GeneralFormFieldType.DATE;
    }

    @Override
    public List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO) {
        List<FlowConditionVariableDTO> dtoList = new ArrayList<>();
        FlowConditionVariableDTO dto = new FlowConditionVariableDTO();

        dto.setFieldType(fieldDTO.getFieldType());
        dto.setDisplayName(fieldDTO.getFieldDisplayName());
        dto.setName(fieldDTO.getFieldName());

        dto.setOperators(FormFieldOperator.getSupportOperatorList(GeneralFormFieldType.DATE).stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));

        dtoList.add(dto);
        return dtoList;
    }

    @Override
    public FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO, String fieldName, String extra) {
        try {
            Map<String, String> map = (Map<String, String>) StringHelper.fromJsonString(fieldDTO.getFieldValue(), Map.class);

            String temp = map.get("text");
            String pattern;
            if (temp.matches(FlowConditionStringVariable.DATE_TIME_REGEX_SLASH)) {
                pattern = "yyyy/MM/dd HH:mm:ss";
                temp += ":00";
            } else if (temp.matches(FlowConditionStringVariable.DATE_TIME_REGEX_CENTER)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
                temp += ":00";
            } else if (temp.matches(FlowConditionStringVariable.DATE_REGEX_SLASH)) {
                pattern = "yyyy/MM/dd HH:mm:ss";
                temp += " 00:00:00";
            } else if (temp.matches(FlowConditionStringVariable.DATE_REGEX_CENTER)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
                temp += " 00:00:00";
            } else {
                pattern = "yyyy/MM/dd HH:mm:ss";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime dateTime = LocalDateTime.parse(temp, formatter);
            return new FlowConditionDateVariable(Date.from(dateTime.toInstant(OffsetDateTime.now().getOffset())));
        } catch (Exception ignored) {
            return null;
            // throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CONDITION_VARIABLE,
            //         "Flow condition variable parse error, fieldDTO=%s", fieldDTO);
        }
    }
}
