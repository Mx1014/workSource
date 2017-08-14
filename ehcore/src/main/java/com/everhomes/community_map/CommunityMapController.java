// @formatter:off
package com.everhomes.community_map;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.community_map.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value="Community map controller", site="community_map")
@RestController
@RequestMapping("/community_map")
public class CommunityMapController extends ControllerBase {

    @Autowired
    private CommunityMapService communityMapService;
    /**
     * <b>URL: /community_map/listCommunityMapSearchTypes</b>
     * <p>根据场景、域空间查查询内容类型</p>
     */
    @RequestMapping("listCommunityMapSearchTypes")
    @RestReturn(value=ListCommunityMapSearchTypesResponse.class)
    public RestResponse listCommunityMapSearchTypes(ListCommunityMapSearchTypesCommand cmd) {
        RestResponse response = new RestResponse(communityMapService.listCommunityMapSearchTypesByScene(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /community_map/searchCommunityMapContents</b>
     * <p>根据场景、查询类型查询相应内容</p>
     */
    @RequestMapping("searchCommunityMapContents")
    @RestReturn(value=SearchCommunityMapContentsResponse.class)
    public RestResponse searchCommunityMapContents(SearchCommunityMapContentsCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /community_map/getCommunityMapBuildingDetailById</b>
     * <p>根据id查询楼栋详情</p>
     */
    @RequestMapping("getCommunityMapBuildingDetailById")
    @RestReturn(value=CommunityMapBuildingDTO.class)
    public RestResponse getCommunityMapBuildingDetailById(GetCommunityMapBuildingDetailByIdCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

}
