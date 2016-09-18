package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>templateTypeId:模板类型id</li>
 * </ul>
 */
public class ListApplyTemplatesCommand {
	
	private Long templateTypeId;

	public Long getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Long templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
