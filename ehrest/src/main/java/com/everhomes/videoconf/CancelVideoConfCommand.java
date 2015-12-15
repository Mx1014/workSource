package com.everhomes.videoconf;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>sourceAccountName: 源账号</li>
 *  <li>confId: 会议id</li>
 *  <li>endTime: 结束时间</li>
 * </ul>
 */
public class CancelVideoConfCommand {

	private Long confId;
	
	private String sourceAccountName;
	
	private Long endTime;

	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
	}

	public String getSourceAccountName() {
		return sourceAccountName;
	}

	public void setSourceAccountName(String sourceAccountName) {
		this.sourceAccountName = sourceAccountName;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
