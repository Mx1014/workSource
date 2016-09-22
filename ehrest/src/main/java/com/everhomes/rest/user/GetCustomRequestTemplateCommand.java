package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>templateType:模板类型</li>
 * </ul>
 */
public class GetCustomRequestTemplateCommand {

	private String templateType;

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
