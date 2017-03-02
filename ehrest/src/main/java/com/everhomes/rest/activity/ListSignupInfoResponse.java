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
	private Long nextPageAnchor;
	@ItemType(SignupInfoDTO.class)
	private List<SignupInfoDTO> signupInfoDTOs;

	public ListSignupInfoResponse() {
		super();
	}

	public ListSignupInfoResponse(Long nextPageAnchor, List<SignupInfoDTO> signupInfoDTOs) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.signupInfoDTOs = signupInfoDTOs;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<SignupInfoDTO> getSignupInfoDTOs() {
		return signupInfoDTOs;
	}

	public void setSignupInfoDTOs(List<SignupInfoDTO> signupInfoDTOs) {
		this.signupInfoDTOs = signupInfoDTOs;
	}

}
