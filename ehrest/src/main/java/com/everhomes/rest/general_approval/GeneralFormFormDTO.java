package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>formFields: 子表单的字段列表</li>
 * </ul>
 * @author janson
 *
 */
public class GeneralFormFormDTO {

    @ItemType(GeneralFormFieldDTO.class)
	private
    List<GeneralFormFieldDTO> formFields;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

 

	public List<GeneralFormFieldDTO> getFormFields() {
		return formFields;
	}


	public void setFormFields(List<GeneralFormFieldDTO> formFields) {
		this.formFields = formFields;
	}
}
