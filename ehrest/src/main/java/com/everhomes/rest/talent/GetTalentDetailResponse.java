// @formatter:off
package com.everhomes.rest.talent;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>talent: 人才信息</li>
 * </ul>
 */
public class GetTalentDetailResponse {

	private TalentDTO talent;

	public GetTalentDetailResponse() {

	}

	public GetTalentDetailResponse(TalentDTO talent) {
		super();
		this.talent = talent;
	}

	public TalentDTO getTalent() {
		return talent;
	}

	public void setTalent(TalentDTO talent) {
		this.talent = talent;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
