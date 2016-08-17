// @formatter: off
package com.everhomes.rest.locale;

/**
 * 
 * <ul>参数
 * <li>scope: 具体的scope</li>
 * <li>code: 对应scope类中的code，比如GroupNotificationTemplateCode.GROUP_MEMBER_DELETED_ADMIN</li>
 * <li>locale: zh_CN</li>
 * <li>description: 描述</li>
 * <li>text: 模板内容</li>
 * <li>namespaceId: 域空间</li>
 * </ul>
 */
public class CreateLocaleTemplateResponse {
	private String scope;
	private Integer code;
	private String locale;
	private String description;
	private String text;
	private Integer namespaceId;

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
}
