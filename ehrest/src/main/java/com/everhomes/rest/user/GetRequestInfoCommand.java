package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id:申请信息主键id</li>
 *  <li>templateType:模板类型</li>
 * </ul>
 */
public class GetRequestInfoCommand {
	
	private Long id;
	
	private String templateType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
