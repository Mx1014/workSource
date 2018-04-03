// @formatter:off
package com.everhomes.rest.messaging;

import com.everhomes.util.StringHelper;

/**
 * 消息占位符的内容，适用于把模板返回给客户端，客户端拼接里面的内容并跳转的情况
 * 例如，${name} 评论了你的帖子 ${title}，name:{content:"tt"}, title:{content:"我是活动",url:"zl://activity/d?forumId=1&topicId=1"}
 * <ul>
 * <li>content: 占位符的内容</li>
 * <li>url: 占位符实际跳转的链接，无url则不跳转</li>
 * </ul>
 */
public class MessageMetaContent {
	private String content;
	private String url;

	public MessageMetaContent() {
		super();
	}

	public MessageMetaContent(String content) {
		super();
		this.content = content;
	}

	public MessageMetaContent(String content, String url) {
		super();
		this.content = content;
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
