// @formatter:off
package com.everhomes.talent;

import java.util.List;

public interface TalentQueryHistoryProvider {

	void createTalentQueryHistory(TalentQueryHistory talentQueryHistory);

	void updateTalentQueryHistory(TalentQueryHistory talentQueryHistory);

	TalentQueryHistory findTalentQueryHistoryById(Long id);

	List<TalentQueryHistory> listTalentQueryHistory();

	List<TalentQueryHistory> listTalentQueryHistoryByUser(Long userId);

	TalentQueryHistory findTalentQueryHistoryByKeyword(Long userId, String keyword);

	void deleteTalentQueryHistory(TalentQueryHistory talentQueryHistory);

}