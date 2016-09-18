package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>requestJson:申请信息json</li>
 *  <li>templateType:模板类型</li>
 * </ul>
 */
public class AddRequestCommand {

	private String requestJson;
	
	private String templateType;


	public String getRequestJson() {
		return requestJson;
	}


	public void setRequestJson(String requestJson) {
		this.requestJson = requestJson;
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
