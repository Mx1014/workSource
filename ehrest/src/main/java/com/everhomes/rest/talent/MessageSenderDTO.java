// @formatter:off
package com.everhomes.rest.talent;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>name: 姓名</li>
 * <li>phone: 手机号</li>
 * </ul>
 */
public class MessageSenderDTO{
	private Long id;
	private String name;
	private String phone;

	public MessageSenderDTO() {
		super();
	}

	public MessageSenderDTO(Long id, String name, String phone) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
