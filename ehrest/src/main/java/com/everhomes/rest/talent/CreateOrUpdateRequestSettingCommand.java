// @formatter:off
package com.everhomes.rest.talent;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>enable: 是否支持申请</li>
 * <li>requestName: 申请按钮名称</li>
 * <li>formId: 表单id</li>
 * </ul>
 */
public class CreateOrUpdateRequestSettingCommand {
	@NotNull
	private Long organizationId;
	private Byte enable;
	private String requestName;
	private Long formId;

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
