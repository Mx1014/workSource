package com.everhomes.rest.payment;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>mobile: 手机号</li>
 * <li>userName: 用户名称</li>
 * <li>createTime: 开卡时间 </li>
 * <li>cardNo: 卡号</li>
 * <li>status: 状态 0无效 2 有效</li>
 * </ul>
 */
public class CardUserDTO {
	private String mobile;
	private String userName;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String cardNo;
	private Byte status;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
