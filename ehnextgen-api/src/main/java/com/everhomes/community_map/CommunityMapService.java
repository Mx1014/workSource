package com.everhomes.community_map;

import com.everhomes.rest.community_map.*;

/**
 * @author sw on 2017/8/14.
 */
public interface CommunityMapService {
    ListCommunityMapSearchTypesResponse listCommunityMapSearchTypesByScene(ListCommunityMapSearchTypesCommand cmd);

    SearchCommunityMapContentsResponse searchContentsByScene(SearchCommunityMapContentsCommand cmd);

    CommunityMapBuildingDetailDTO getCommunityMapBuildingDetailById(GetCommunityMapBuildingDetailByIdCommand cmd);
}
