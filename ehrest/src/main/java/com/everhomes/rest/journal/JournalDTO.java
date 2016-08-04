package com.everhomes.rest.journal;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>title: 主题</li>
 * <li>contentType: content类型 {@link com.everhomes.rest.journal.JournalContentType} </li>
 * <li>content: 关联链接</li>
 * <li>coverUri: 封面路径</li>
 * <li>creatorUid: 发行人ID</li>
 * <li>creatorName: 发行人名称</li>
 * <li>createTime: 发行时间</li>
 * </ul>
 */
public class JournalDTO {
	private Long id;
	private String title;
	private Byte contentType;
	private String content;
	private String coverUri;
	private Long creatorUid;
	private String creatorName;
	private Timestamp createTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}
	public Byte getContentType() {
		return contentType;
	}
	public void setContentType(Byte contentType) {
		this.contentType = contentType;
	}
}
