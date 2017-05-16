package com.everhomes.rest.general_form;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_form.GeneralFormDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forms: 表单列表 {@link GeneralFormDTO}</li>
 * </ul>
 * @author janson
 *
 */
public class ListGeneralFormResponse {
	@ItemType(GeneralFormDTO.class)
	List<GeneralFormDTO> forms;

	public List<GeneralFormDTO> getForms() {
		return forms;
	}

	public void setForms(List<GeneralFormDTO> forms) {
		this.forms = forms;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
