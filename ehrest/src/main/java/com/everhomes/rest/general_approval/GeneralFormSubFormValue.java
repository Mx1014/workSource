package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class GeneralFormSubFormValue {

    @ItemType(GeneralFormSubFormValueDTO.class)
    private List<GeneralFormSubFormValueDTO> subForms;

    public GeneralFormSubFormValue() {
    }

    public List<GeneralFormSubFormValueDTO> getSubForms() {
        return subForms;
    }

    public void setSubForms(List<GeneralFormSubFormValueDTO> subForms) {
        this.subForms = subForms;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
