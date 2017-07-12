// @formatter:off
package com.everhomes.rest.messaging;

import com.everhomes.util.StringHelper;

/**
 * 内部有链接的消息的消息体
 * <ul>
 * <li>title: 标题</li>
 * <li>template: 模板</li>
 * </ul>
 */
public class InnerLinkBody {
	private String title;
	private String template;

	public InnerLinkBody() {
		super();
	}

	public InnerLinkBody(String title, String template) {
		super();
		this.title = title;
		this.template = template;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
