package com.everhomes.journal;

import com.everhomes.rest.journal.CreateJournalCommand;
import com.everhomes.rest.journal.DeleteJournalCommand;
import com.everhomes.rest.journal.GetJournalCommand;
import com.everhomes.rest.journal.GetJournalConfigCommand;
import com.everhomes.rest.journal.JournalConfigDTO;
import com.everhomes.rest.journal.JournalDTO;
import com.everhomes.rest.journal.ListJournalsCommand;
import com.everhomes.rest.journal.ListJournalsResponse;
import com.everhomes.rest.journal.UpdateJournalCommand;
import com.everhomes.rest.journal.UpdateJournalConfigCommand;

public interface JournalService {
	ListJournalsResponse listJournals(ListJournalsCommand cmd);
	
	JournalDTO getJournal(GetJournalCommand cmd);
	
	void updateJournal(UpdateJournalCommand cmd);
	
	void createJournal(CreateJournalCommand cmd);
	
	void deleteJournal(DeleteJournalCommand cmd);
	
	void updateJournalConfig(UpdateJournalConfigCommand cmd);
	
	JournalConfigDTO getJournalConfig(GetJournalConfigCommand cmd);
}
