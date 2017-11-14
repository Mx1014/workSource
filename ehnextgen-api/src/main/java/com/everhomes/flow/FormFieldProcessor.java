package com.everhomes.flow;

import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;

import java.util.List;

/**
 * Created by xq.tian on 2017/11/3.
 */
public interface FormFieldProcessor {

    GeneralFormFieldType getSupportFieldType();

    List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO);

    FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO);

    default String parseFormFieldName(FlowCaseState ctx, String variable, String extra) {
        return variable;
    }
}
