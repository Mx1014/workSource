// @formatter:off
package com.everhomes.talent;

import java.util.List;

public interface TalentMessageSenderProvider {

	void createTalentMessageSender(TalentMessageSender talentMessageSender);

	void updateTalentMessageSender(TalentMessageSender talentMessageSender);

	TalentMessageSender findTalentMessageSenderById(Long id);

	List<TalentMessageSender> listTalentMessageSender();

	List<TalentMessageSender> listTalentMessageSenderByOwner(String ownerType, Long ownerId);

}