// @formatter:off
package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * 
 * <ul>
 * <li>nextPageOffset: 下页页码</li>
 * <li>unConfirmCount: 未确认总数</li>
 * <li>signupInfoDTOs: 报名信息列表，参考{@link com.everhomes.rest.activity.SignupInfoDTO}</li>
 * </ul>
 */
public class ListSignupInfoResponse {
	private Integer nextPageOffset;
	private Integer unConfirmCount;
	@ItemType(SignupInfoDTO.class)
	private List<SignupInfoDTO> signupInfoDTOs;

	public ListSignupInfoResponse() {
		super();
	}

	public ListSignupInfoResponse(Integer nextPageOffset, Integer unConfirmCount, List<SignupInfoDTO> signupInfoDTOs) {
		super();
		this.nextPageOffset = nextPageOffset;
		this.unConfirmCount = unConfirmCount;
		this.signupInfoDTOs = signupInfoDTOs;
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public Integer getUnConfirmCount() {
		return unConfirmCount;
	}

	public void setUnConfirmCount(Integer unConfirmCount) {
		this.unConfirmCount = unConfirmCount;
	}

	public List<SignupInfoDTO> getSignupInfoDTOs() {
		return signupInfoDTOs;
	}

	public void setSignupInfoDTOs(List<SignupInfoDTO> signupInfoDTOs) {
		this.signupInfoDTOs = signupInfoDTOs;
	}

}
