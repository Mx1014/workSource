// @formatter:off
package com.everhomes.rest.talent;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>talentQueryHistories: 历史记录</li>
 * </ul>
 */
public class ListTalentQueryHistoryResponse {

	@ItemType(TalentQueryHistoryDTO.class)
	private List<TalentQueryHistoryDTO> talentQueryHistories;

	public ListTalentQueryHistoryResponse() {

	}

	public ListTalentQueryHistoryResponse(List<TalentQueryHistoryDTO> talentQueryHistories) {
		super();
		this.talentQueryHistories = talentQueryHistories;
	}

	public List<TalentQueryHistoryDTO> getTalentQueryHistories() {
		return talentQueryHistories;
	}

	public void setTalentQueryHistories(List<TalentQueryHistoryDTO> talentQueryHistories) {
		this.talentQueryHistories = talentQueryHistories;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
