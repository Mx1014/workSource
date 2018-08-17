package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>phone: phone</li>
 *     <li>regionCode: regionCode</li>
 *     <li>userId: 用户ID</li>
 * </ul>
 */
public class VerificationCodeForBindPhoneCommand {

	private String phone;

	private Integer regionCode;

	private Long userId;

	public VerificationCodeForBindPhoneCommand() {
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(Integer regionCode) {
		this.regionCode = regionCode;
	}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
