// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * 
 * <ul>
 * <li>nextAnchor: 下页页码</li>
 * <li>signupInfoDTOs: 报名信息列表，参考{@link SignupInfoDTO}</li>
 * </ul>
 */
public class ListSignupInfoByOrganizationIdResponse {
	private Long nextAnchor;
	@ItemType(SignupInfoDTO.class)
	private List<SignupInfoDTO> signupInfoDTOs;

	public Long getNextAnchor() {
		return nextAnchor;
	}

	public void setNextAnchor(Long nextAnchor) {
		this.nextAnchor = nextAnchor;
	}

	public List<SignupInfoDTO> getSignupInfoDTOs() {
		return signupInfoDTOs;
	}

	public void setSignupInfoDTOs(List<SignupInfoDTO> signupInfoDTOs) {
		this.signupInfoDTOs = signupInfoDTOs;
	}

}
