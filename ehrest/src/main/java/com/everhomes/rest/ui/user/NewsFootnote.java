package com.everhomes.rest.ui.user;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>createTime: 发布时间</li>
 * <li>author: 作者</li>
 * <li>sourceDesc: 来源</li>
 * <li>newsToken: 新闻标识</li>
 *</ul>
 */
public class NewsFootnote {

	private Timestamp createTime;
	
	private String author;
	
	private String sourceDesc;
	
	private String newsToken;

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSourceDesc() {
		return sourceDesc;
	}

	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}
	
	public String getNewsToken() {
		return newsToken;
	}

	public void setNewsToken(String newsToken) {
		this.newsToken = newsToken;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
