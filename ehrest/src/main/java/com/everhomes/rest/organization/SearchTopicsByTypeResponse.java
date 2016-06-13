package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SearchTopicsByTypeResponse {

	private Long nextPageAnchor;

    @ItemType(OrganizationTaskDTO2.class)
    private List<OrganizationTaskDTO2> requests;
    
    private String keywords;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<OrganizationTaskDTO2> getRequests() {
		return requests;
	}

	public void setRequests(List<OrganizationTaskDTO2> requests) {
		this.requests = requests;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
