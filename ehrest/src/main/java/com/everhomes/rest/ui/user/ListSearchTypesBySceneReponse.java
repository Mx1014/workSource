package com.everhomes.rest.ui.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 *<ul> 
 * <li>searchTypes: 搜索类型，{@link com.everhomes.rest.ui.user.SearchTypeDTO}</li>
 *</ul>
 */
public class ListSearchTypesBySceneReponse {

	@ItemType(SearchTypeDTO.class)
	private List<SearchTypeDTO> searchTypes;

	public List<SearchTypeDTO> getSearchTypes() {
		return searchTypes;
	}

	public void setSearchTypes(List<SearchTypeDTO> searchTypes) {
		this.searchTypes = searchTypes;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
