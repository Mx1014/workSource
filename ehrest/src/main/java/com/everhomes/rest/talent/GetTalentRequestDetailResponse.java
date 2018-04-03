// @formatter:off
package com.everhomes.rest.talent;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>talentRequest: 申请记录，参考{@link com.everhomes.rest.talent.TalentRequestDTO}</li>
 * </ul>
 */
public class GetTalentRequestDetailResponse {

	private TalentRequestDTO talentRequest;

	public GetTalentRequestDetailResponse() {

	}

	public GetTalentRequestDetailResponse(TalentRequestDTO talentRequest) {
		super();
		this.talentRequest = talentRequest;
	}

	public TalentRequestDTO getTalentRequest() {
		return talentRequest;
	}

	public void setTalentRequest(TalentRequestDTO talentRequest) {
		this.talentRequest = talentRequest;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
