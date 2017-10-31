package com.everhomes.flow;

import com.everhomes.rest.flow.FlowConditionRelationalOperatorType;
import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;

import java.util.List;

/**
 * Created by xq.tian on 2017/11/3.
 */
public interface FormFieldProcessor {

    GeneralFormFieldType getSupportFieldType();

    List<FlowConditionRelationalOperatorType> getSupportOperatorList();

    List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO);

    FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO);
}
