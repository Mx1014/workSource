// @formatter:off
package com.everhomes.rest.openapi.jindi;

import java.sql.Timestamp;

/**
 * 
 * <ul>金地同步数据的活动数据
 * <li>: </li>
 * </ul>
 */
public class JindiActionActivityDTO extends JindiDataDTO {
	private Long id;
	private Long userId;
	private String userName;
	private String phone;
	private String subject;
	private Timestamp startTime;
	private Byte status;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

}
