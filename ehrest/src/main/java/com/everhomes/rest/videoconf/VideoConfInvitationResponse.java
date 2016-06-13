package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>subject: 邮件主题</li>
 *  <li>body: 邮件正文</li>
 * </ul>
 *
 */
public class VideoConfInvitationResponse {
	
	private String subject;
	
	private String body;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
