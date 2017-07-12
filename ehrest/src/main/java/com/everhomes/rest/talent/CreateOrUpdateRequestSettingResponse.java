// @formatter:off
package com.everhomes.rest.talent;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>enable: 是否支持申请</li>
 * <li>requestName: 申请按钮名称</li>
 * <li>formId: 表单id</li>
 * </ul>
 */
public class CreateOrUpdateRequestSettingResponse {	
	@NotNull
	private Long organizationId;
	private Byte enable;
	private String requestName;
	private Long formId;

	public CreateOrUpdateRequestSettingResponse() {
		super();
	}

	public CreateOrUpdateRequestSettingResponse(Byte enable, String requestName, Long formId) {
		super();
		this.enable = enable;
		this.requestName = requestName;
		this.formId = formId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Byte getEnable() {
		return enable;
	}

	public void setEnable(Byte enable) {
		this.enable = enable;
	}

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

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
