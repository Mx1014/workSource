package com.everhomes.rest.journal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListJournalsResponse {
	
    private Long nextPageAnchor;
	
    @ItemType(JournalDTO.class)
	private List<JournalDTO> journals;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<JournalDTO> getJournals() {
		return journals;
	}

	public void setJournals(List<JournalDTO> journals) {
		this.journals = journals;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
} 
