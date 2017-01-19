package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * templateId: 模板的主键id
 *
 */
public class DeleteUserQualityInspectionTaskTemplateCommand {

	@NotNull
	private Long templateId;

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
