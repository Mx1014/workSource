// @formatter:off
package com.everhomes.rest.locale;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: 记录id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>scope: 范围</li>
 * <li>code: 代码</li>
 * <li>locale: 语言</li>
 * <li>text: 模板文本</li>
 * </ul>
 */
public class LocaleTemplateDTO {
	private Long id;
	private Integer namespaceId;
	private String scope;
	private Integer code;
	private String locale;
	private String text;
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
