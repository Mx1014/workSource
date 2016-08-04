package com.everhomes.journal;

import java.util.List;


public interface JournalProvider {
	List<Journal> listJournals(Integer namespaceId, String keyword, Long pageAnchor, Integer pageSize);
	Journal findJournal(Long id);
	void updateJournal(Journal journal);
	void updateJournalConfig(JournalConfig journalConfig);
	JournalConfig findJournalConfig(Integer namespaceId);
	void createJournal(Journal journal);
}
