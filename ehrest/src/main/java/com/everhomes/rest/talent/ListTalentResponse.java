// @formatter:off
package com.everhomes.rest.talent;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>talents: 人才列表，参考{@link com.everhomes.rest.talent.TalentDTO}</li>
 * </ul>
 */
public class ListTalentResponse {

	private Long nextPageAnchor;

	@ItemType(TalentDTO.class)
	private List<TalentDTO> talents;

	public ListTalentResponse() {

	}

	public ListTalentResponse(Long nextPageAnchor, List<TalentDTO> talents) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.talents = talents;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<TalentDTO> getTalents() {
		return talents;
	}

	public void setTalents(List<TalentDTO> talents) {
		this.talents = talents;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
