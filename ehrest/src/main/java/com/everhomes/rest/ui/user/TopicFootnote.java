package com.everhomes.rest.ui.user;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>creatorNickName: 发布人</li>
 * <li>createTime: 发布时间</li>
 *</ul>
 */
public class TopicFootnote {

	private String creatorNickName;
	
	private String createTime;
	
	public TopicFootnote() {
		this.createTime = "";
		this.creatorNickName = "";
    }

	public String getCreatorNickName() {
		return creatorNickName;
	}

	public void setCreatorNickName(String creatorNickName) {
		this.creatorNickName = creatorNickName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
