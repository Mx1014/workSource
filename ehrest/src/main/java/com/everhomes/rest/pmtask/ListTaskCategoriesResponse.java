package com.everhomes.rest.pmtask;

import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.category.CategoryDTO;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页瞄</li>
 * <li>requests: 列表 {@link com.everhomes.rest.category.CategoryDTO}</li>
 * </ul>
 */
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
