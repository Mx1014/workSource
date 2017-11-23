// @formatter:off
package com.everhomes.rest.talent;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>talentRequests: 申请列表，参考{@link com.everhomes.rest.talent.TalentRequestDTO}</li>
 * </ul>
 */
public class ListTalentRequestResponse {

	private Long nextPageAnchor;

	@ItemType(TalentRequestDTO.class)
	private List<TalentRequestDTO> talentRequests;

	public ListTalentRequestResponse() {

	}

	public ListTalentRequestResponse(Long nextPageAnchor, List<TalentRequestDTO> talentRequests) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.talentRequests = talentRequests;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<TalentRequestDTO> getTalentRequests() {
		return talentRequests;
	}

	public void setTalentRequests(List<TalentRequestDTO> talentRequests) {
		this.talentRequests = talentRequests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
