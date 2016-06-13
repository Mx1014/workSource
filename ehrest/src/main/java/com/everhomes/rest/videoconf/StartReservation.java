package com.everhomes.rest.videoconf;


import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>loginName: 用户账号</li>
 * <li>timeStamp: 时间戳</li>
 * <li>token: 请求 token，用户账号+ “|”+该企业分配的密钥  +  “|”  +  timestamp参数值的 MD5 值 </li>
 * <li>confName: 会议名称</li>
 * <li>hostKey: 会议密码</li>
 * <li>startTime: 开始时间</li>
 * <li>duration: 会议持续时间（分钟）</li>
 * <li>optionJbh: 是否允许先于主持人入会  0  否  1  是 </li>
 * </ul>
 */
@XmlRootElement
public class StartReservation {
	
	private String loginName;
	
	private String timeStamp;
	
	private String token;
	
	private String confName;
	
	private String hostKey;
	
	private Date startTime;
	
	private Integer duration;
	
	private Integer optionJbh;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getConfName() {
		return confName;
	}

	public void setConfName(String confName) {
		this.confName = confName;
	}

	public String getHostKey() {
		return hostKey;
	}

	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getOptionJbh() {
		return optionJbh;
	}

	public void setOptionJbh(Integer optionJbh) {
		this.optionJbh = optionJbh;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
