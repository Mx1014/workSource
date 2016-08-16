package com.everhomes.rest.journal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>title: 主题</li>
 * <li>contentType: content类型  1: 链接{@link com.everhomes.rest.journal.JournalContentType} </li>
 * <li>content: 链接</li>
 * <li>coverUri: 封面路径</li>
 * </ul>
 */
public class CreateJournalCommand {
	private Integer namespaceId;
	private String title;
	private Byte contentType;
	private String content;
	private String coverUri;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Byte getContentType() {
		return contentType;
	}
	public void setContentType(Byte contentType) {
		this.contentType = contentType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCoverUri() {
		return coverUri;
	}
	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
