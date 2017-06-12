// @formatter:off
package com.everhomes.talent;

import java.util.List;

import com.everhomes.rest.talent.ListTalentCommand;

public interface TalentProvider {

	void createTalent(Talent talent);

	void updateTalent(Talent talent);

	Talent findTalentById(Long id);

	List<Talent> listTalent();

	void updateTalentId(Talent talent);

	List<Talent> listTalent(Integer namespaceId, ListTalentCommand cmd);

	void updateToOther(Long categoryId);

}