// @formatter:off
package com.everhomes.yellowPage;

import java.util.List;

public interface AllianceExtraEventAttachProvider {

	void createAllianceExtraEventAttach(AllianceExtraEventAttachment allianceExtraEventAttachment);

	List<AllianceExtraEventAttachment> listAttachmentsByEventId(Long eventId);
}
	
