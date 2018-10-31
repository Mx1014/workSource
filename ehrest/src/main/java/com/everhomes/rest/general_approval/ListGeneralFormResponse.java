package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>forms: 表单列表 {@link GeneralFormDTO}</li>
 * <li>nonEditableFieldTypes: 不支持编辑的表单组件类型</li>
 * </ul>
 * @author janson
 *
 */
public class ListGeneralFormResponse {
	@ItemType(GeneralFormDTO.class)
	List<GeneralFormDTO> forms;
	List<String> nonEditableFieldTypes;

	public List<GeneralFormDTO> getForms() {
		return forms;
	}

	public void setForms(List<GeneralFormDTO> forms) {
		this.forms = forms;
	}

	public List<String> getNonEditableFieldTypes() {
		return nonEditableFieldTypes;
	}

	public void setNonEditableFieldTypes(List<String> nonEditableFieldTypes) {
		this.nonEditableFieldTypes = nonEditableFieldTypes;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
