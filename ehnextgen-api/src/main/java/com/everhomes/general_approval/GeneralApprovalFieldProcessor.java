package com.everhomes.general_approval;

import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;

import java.util.List;

public interface GeneralApprovalFieldProcessor {

    void processMultiLineTextField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal);

    void processDropBoxField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal);

    void processImageField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal);

    void processFileField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal);

    void processIntegerTextField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal);

    void processSubFormField(List<FlowCaseEntity> entities, GeneralFormFieldDTO dto, String jsonVal);

    void processSubFormField4EnterpriseApproval(List<FlowCaseEntity> entities, GeneralFormFieldDTO dto, String jsonVal);

    void processNullSubFormField(List<FlowCaseEntity> entities, GeneralFormFieldDTO dto);

    void processContactField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal);

    boolean requiredFlag(List<GeneralFormFieldDTO> formFields);

    boolean sinceVersion510();

    void processMultiSelectField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal);

}


