package com.everhomes.flow;

import com.everhomes.rest.flow.FlowConditionVariableDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;

import java.util.List;

public interface FormFieldProcessorManager {

    List<FlowConditionVariableDTO> convertFieldDtoToFlowConditionVariableDto(Flow flow, GeneralFormFieldDTO fieldDTO);

    FlowConditionVariable getFlowConditionVariable(GeneralFormFieldDTO fieldDTO);
}
