package com.everhomes.rest.pmtask;

import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.category.CategoryDTO;

public class ListTaskCategoriesResponse {
	private Long nextPageAnchor;
	@ItemType(CategoryDTO.class)
	private List<CategoryDTO> requests;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<CategoryDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<CategoryDTO> requests) {
		this.requests = requests;
	}
	
	@Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
