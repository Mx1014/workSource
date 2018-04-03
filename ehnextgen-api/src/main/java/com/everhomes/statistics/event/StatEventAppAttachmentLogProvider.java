// @formatter:off
package com.everhomes.statistics.event;

public interface StatEventAppAttachmentLogProvider {

	void createStatEventAppAttachmentLog(StatEventAppAttachmentLog statEventAppAttachmentLog);

	void updateStatEventAppAttachmentLog(StatEventAppAttachmentLog statEventAppAttachmentLog);

	StatEventAppAttachmentLog findById(Long id);

}