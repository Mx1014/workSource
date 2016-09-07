// @formatter:off
package com.everhomes.rest.approval;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>nickName: 用户昵称</li>
 * <li>punchDate: 打卡日期</li>
 * <li>punchDetail: 打卡详情</li>
 * <li>punchStatusName: 打卡状态名称</li>
 * </ul>
 */
public class ForgotBasicDescription {
	private String nickName;
	private Timestamp punchDate;
	private String punchDetail;
	private String punchStatusName;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Timestamp getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Timestamp punchDate) {
		this.punchDate = punchDate;
	}

	public String getPunchDetail() {
		return punchDetail;
	}

	public void setPunchDetail(String punchDetail) {
		this.punchDetail = punchDetail;
	}

	public String getPunchStatusName() {
		return punchStatusName;
	}

	public void setPunchStatusName(String punchStatusName) {
		this.punchStatusName = punchStatusName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
