package com.everhomes.rest.ui.user;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>creatorNickName: 发布人</li>
 * <li>createTime: 发布时间</li>
 *</ul>
 */
public class PollFootnote {

	private String creatorNickName;
	
	private Timestamp createTime;

	public String getCreatorNickName() {
		return creatorNickName;
	}

	public void setCreatorNickName(String creatorNickName) {
		this.creatorNickName = creatorNickName;
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
}
