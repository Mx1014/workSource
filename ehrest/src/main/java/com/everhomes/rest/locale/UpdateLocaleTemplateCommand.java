// @formatter:off
package com.everhomes.rest.locale;

/**
 * 
 * <ul>
 * <li>id: 记录id</li>
 * <li>text: 模板文本</li>
 * <li>description: 描述</li>
 * </ul>
 */
public class UpdateLocaleTemplateCommand {
	private Long id;
	private String text;
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
