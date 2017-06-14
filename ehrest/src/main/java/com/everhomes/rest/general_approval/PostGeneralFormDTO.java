package com.everhomes.rest.general_approval;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class PostGeneralFormDTO {
    @ItemType(GeneralFormFieldDTO.class)
    private List<GeneralFormFieldDTO> formFields;

    @ItemType(PostApprovalFormItem.class)
    private List<PostApprovalFormItem> values;

    public List<GeneralFormFieldDTO> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<GeneralFormFieldDTO> formFields) {
        this.formFields = formFields;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
