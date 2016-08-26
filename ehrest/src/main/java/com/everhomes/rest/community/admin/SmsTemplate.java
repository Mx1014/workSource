// @formatter:off
package com.everhomes.rest.community.admin;

/**
 * 
 * <ul>
 * <li>code: code</li>
 * <li>title: 标题</li>
 * <li>templateId: 模板id</li>
 * </ul>
 */
public class SmsTemplate {
	private Integer code;
	private String title;
	private String templateId;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
