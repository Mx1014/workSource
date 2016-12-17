// @formatter:off
package com.everhomes.rest.openapi.jindi;

import java.sql.Timestamp;

/**
 * 
 * <ul>金地同步数据的工位预订数据
 * <li>: </li>
 * </ul>
 */
public class JindiActionStationRentalDTO extends JindiDataDTO {
	private Long id;
	private Long userId;
	private String userName;
	private String phone;
	private Timestamp reserveTime;
	private String spaceName;
	private Byte status;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Timestamp getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(Timestamp reserveTime) {
		this.reserveTime = reserveTime;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

}
