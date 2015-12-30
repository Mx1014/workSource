package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>confName: 会议名</li>
 *  <li>confId: 会议id</li>
 *  <li>confTime: 会议时间</li>
 *  <li>channel: 发送邀请方式 0 –email; 1 –message; 2 -wechat</li>
 *  <li>invitee: 被邀请人的email地址或手机号或微信号</li>
 * </ul>
 *
 */
public class CreateVideoConfInvitationCommand {
	
	private String confName;
	
	private Integer confId;
	
	private Long confTime;
	
	private Integer duration;
	
	private Byte channel;
	
	@ItemType(String.class)
	private List<String> invitee;

	public String getConfName() {
		return confName;
	}

	public void setConfName(String confName) {
		this.confName = confName;
	}

	public Integer getConfId() {
		return confId;
	}

	public void setConfId(Integer confId) {
		this.confId = confId;
	}

	public Long getConfTime() {
		return confTime;
	}

	public void setConfTime(Long confTime) {
		this.confTime = confTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Byte getChannel() {
		return channel;
	}

	public void setChannel(Byte channel) {
		this.channel = channel;
	}

	public List<String> getInvitee() {
        return invitee;
    }

    public void setInvitee(List<String> invitee) {
        this.invitee = invitee;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
