package com.everhomes.flow.formfield;

import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowConditionVariable;
import com.everhomes.flow.FormFieldProcessor;
import com.everhomes.flow.conditionvariable.FlowConditionDateVariable;
import com.everhomes.rest.flow.FlowConditionRelationalOperatorType;
import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
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
public class FormFieldDateProcessor implements FormFieldProcessor {

    @Override
    public GeneralFormFieldType getSupportFieldType() {
        return GeneralFormFieldType.DATE;
    }

    protected List<FlowConditionRelationalOperatorType> getSupportOperatorList() {
        List<FlowConditionRelationalOperatorType> operatorTypes = new ArrayList<>();
        operatorTypes.add(FlowConditionRelationalOperatorType.EQUAL);
        operatorTypes.add(FlowConditionRelationalOperatorType.NOT_EQUAL);
        operatorTypes.add(FlowConditionRelationalOperatorType.GREATER_THEN);
        operatorTypes.add(FlowConditionRelationalOperatorType.GREATER_OR_EQUAL);
        operatorTypes.add(FlowConditionRelationalOperatorType.LESS_THEN);
        operatorTypes.add(FlowConditionRelationalOperatorType.LESS_OR_EQUAL);
        return operatorTypes;
    }

    @Override
    public List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO) {
        List<FlowConditionVariableDTO> dtoList = new ArrayList<>();
        FlowConditionVariableDTO dto = new FlowConditionVariableDTO();

        dto.setFieldType(fieldDTO.getFieldType());
        dto.setDisplayName(fieldDTO.getFieldDisplayName());
        dto.setName(fieldDTO.getFieldName());

        dto.setOperators(getSupportOperatorList().stream().map(FlowConditionRelationalOperatorType::getCode).collect(Collectors.toList()));

        dtoList.add(dto);
        return dtoList;
    }

    @Override
    public FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO) {
        try {
            Map<String, String> map = (Map<String, String>) StringHelper.fromJsonString(fieldDTO.getFieldValue(), Map.class);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            TemporalAccessor accessor = formatter.parse(map.get("text"));
            LocalDateTime dateTime = LocalDateTime.from(accessor);
            return new FlowConditionDateVariable(java.sql.Date.from(dateTime.toInstant(ZoneOffset.UTC)));
        } catch (Exception ignored) {
            return null;
            // throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CONDITION_VARIABLE,
            //         "Flow condition variable parse error, fieldDTO=%s", fieldDTO);
        }
    }
}
