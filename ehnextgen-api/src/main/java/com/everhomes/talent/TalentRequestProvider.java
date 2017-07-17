// @formatter:off
package com.everhomes.talent;

import java.util.List;

import com.everhomes.rest.talent.ListTalentRequestCommand;

public interface TalentRequestProvider {

	void createTalentRequest(TalentRequest talentRequest);

	void updateTalentRequest(TalentRequest talentRequest);

	TalentRequest findTalentRequestById(Long id);

	List<TalentRequest> listTalentRequest();

	List<TalentRequest> listTalentRequestByCondition(Integer namespaceId, ListTalentRequestCommand cmd);

}