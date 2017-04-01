// @formatter:off
package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * 
 * <ul>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>signupInfoDTOs: 报名信息列表，参考{@link com.everhomes.rest.activity.SignupInfoDTO}</li>
 * </ul>
 */
public class ListSignupInfoResponse {
	private Integer nextPageOffset;
	@ItemType(SignupInfoDTO.class)
	private List<SignupInfoDTO> signupInfoDTOs;

	public ListSignupInfoResponse() {
		super();
	}

	public ListSignupInfoResponse(Integer nextPageOffset, List<SignupInfoDTO> signupInfoDTOs) {
		super();
		this.nextPageOffset = nextPageOffset;
		this.signupInfoDTOs = signupInfoDTOs;
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<SignupInfoDTO> getSignupInfoDTOs() {
		return signupInfoDTOs;
	}

	public void setSignupInfoDTOs(List<SignupInfoDTO> signupInfoDTOs) {
		this.signupInfoDTOs = signupInfoDTOs;
	}

}
