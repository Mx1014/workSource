package com.everhomes.rest.wx;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>uid: uid</li>
 *     <li>status: 是否登录，0-否，1-是</li>
 *     <li>loginToken: loginToken</li>
 *     <li>contentServer: contentServer</li>
 * </ul>
 */
public class CheckAuthResponse {

	private long uid;
	private Byte status;
	private String loginToken;
	private String contentServer;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getContentServer() {
		return contentServer;
	}

	public void setContentServer(String contentServer) {
		this.contentServer = contentServer;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
