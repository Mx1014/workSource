package com.everhomes.rest.community_map;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/8/14.
 */
public class ListCommunityMapSearchTypesResponse {
    @ItemType(CommunityMapSearchTypeDTO.class)
    private List<CommunityMapSearchTypeDTO> searchTypes;

    public List<CommunityMapSearchTypeDTO> getSearchTypes() {
        return searchTypes;
    }

    public void setSearchTypes(List<CommunityMapSearchTypeDTO> searchTypes) {
        this.searchTypes = searchTypes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
