package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul> 获取表单信息
 * <li>formId: 表单的 ID</li>
 * </ul>
 * @author janson
 *
 */
public class GetTemplateByFormIdCommand {
	private Long formId;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
