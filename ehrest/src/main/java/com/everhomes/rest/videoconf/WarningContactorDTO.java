package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>contactor: 预警人姓名  </li>
 *  <li>mobile: 预警人手机号</li>
 *  <li>email: 预警人邮箱</li>
 * </ul>
 *
 */
public class WarningContactorDTO {

	private Long id;
	
	private String contactor;
	
	private String mobile;
	
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
