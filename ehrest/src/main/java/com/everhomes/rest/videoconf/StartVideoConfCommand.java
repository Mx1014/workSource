package com.everhomes.rest.videoconf;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>accountId: 账号id</li>
 * <li>confName: 会议名称</li>
 * <li>password: 会议密码</li>
 * <li>startTime: 开始时间</li>
 * <li>duration: 会议持续时间（分钟）</li>
 * </ul>
 */
public class StartVideoConfCommand {

	private Long accountId;
	
	private String password;
	
	private String  confName;
	
	private Long startTime;
	
	private Integer duration;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfName() {
		return confName;
	}

	public void setConfName(String confName) {
		this.confName = confName;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
