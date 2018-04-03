package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>formFields: 子表单的字段列表{@link com.everhomes.rest.general_approval.GeneralFormFieldDTO}}</li>
 * </ul>
 * @author copy from GeneralFormSubformDTO
 *
 */
public class GeneralFormSubFormValueDTO {

    @ItemType(GeneralFormFieldDTO.class)
    private List<GeneralFormFieldDTO> formFields;

    public GeneralFormSubFormValueDTO() {
    }

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
