package com.everhomes.community_map;

import com.everhomes.rest.community_map.ListCommunityMapSearchTypesCommand;
import com.everhomes.rest.community_map.ListCommunityMapSearchTypesResponse;
import com.everhomes.rest.community_map.SearchCommunityMapContentsCommand;
import com.everhomes.rest.community_map.SearchCommunityMapContentsResponse;

/**
 * @author sw on 2017/8/14.
 */
public interface CommunityMapService {
    ListCommunityMapSearchTypesResponse listCommunityMapSearchTypesByScene(ListCommunityMapSearchTypesCommand cmd);

    SearchCommunityMapContentsResponse searchContentsByScene(SearchCommunityMapContentsCommand cmd);
}
