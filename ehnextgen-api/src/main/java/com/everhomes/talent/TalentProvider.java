// @formatter:off
package com.everhomes.talent;

import java.util.List;

public interface TalentProvider {

	void createTalent(Talent talent);

	void updateTalent(Talent talent);

	Talent findTalentById(Long id);

	List<Talent> listTalent();

}