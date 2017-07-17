// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface StatEventAppLogAttachmentProvider {

	void createStatEventAppLogAttachment(StatEventAppLogAttachment statEventAppLogAttachment);

	void updateStatEventAppLogAttachment(StatEventAppLogAttachment statEventAppLogAttachment);

	StatEventAppLogAttachment findStatEventAppLogAttachmentById(Long id);

	// List<StatEventAppLogAttachment> listStatEventAppLogAttachment();

}