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
        RestResponse response = new RestResponse(communityMapService.searchContentsByScene(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /community_map/getCommunityMapBuildingDetailById</b>
     * <p>根据id查询楼栋详情</p>
     */
    @RequestMapping("getCommunityMapBuildingDetailById")
    @RestReturn(value=CommunityMapBuildingDetailDTO.class)
    public RestResponse getCommunityMapBuildingDetailById(GetCommunityMapBuildingDetailByIdCommand cmd) {
        RestResponse response = new RestResponse(communityMapService.getCommunityMapBuildingDetailById(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /community_map/getCommunityMapInitData</b>
     * <p>获取园区地图初始化数据</p>
     */
    @RequestMapping("getCommunityMapInitData")
    @RestReturn(value=CommunityMapInitDataDTO.class)
    public RestResponse getCommunityMapInitData(GetCommunityMapInitDataCommand cmd) {
        RestResponse response = new RestResponse(communityMapService.getCommunityMapInitData(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /community_map/searchCommunityMapShops</b>
     * <p>搜索园区地图店铺</p>
     */
    @RequestMapping("searchCommunityMapShops")
    @RestReturn(value=SearchCommunityMapShopsResponse.class)
    public RestResponse searchCommunityMapShops(SearchCommunityMapShopsCommand cmd) {
        RestResponse response = new RestResponse(communityMapService.searchCommunityMapShops(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /community_map/getCommunityMapShopDetailById</b>
     * <p>根据ID园区地图店铺</p>
     */
    @RequestMapping("getCommunityMapShopDetailById")
    @RestReturn(value=CommunityMapShopDetailDTO.class)
    public RestResponse getCommunityMapShopDetailById(GetCommunityMapShopDetailByIdCommand cmd) {
        RestResponse response = new RestResponse(communityMapService.getCommunityMapShopDetailById(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /community_map/updateCommunityMapShop</b>
     * <p>修改园区地图店铺</p>
     */
    @RequestMapping("updateCommunityMapShop")
    @RestReturn(value=CommunityMapShopDetailDTO.class)
    public RestResponse createCommunityMapShop(CreateCommunityMapShopCommand cmd) {
        RestResponse response = new RestResponse(communityMapService.createCommunityMapShop(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /community_map/updateCommunityMapShop</b>
     * <p>修改园区地图店铺</p>
     */
    @RequestMapping("updateCommunityMapShop")
    @RestReturn(value=CommunityMapShopDetailDTO.class)
    public RestResponse updateCommunityMapShop(UpdateCommunityMapShopCommand cmd) {
        RestResponse response = new RestResponse(communityMapService.updateCommunityMapShop(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /community_map/deleteCommunityMapShop</b>
     * <p>删除园区地图店铺</p>
     */
    @RequestMapping("deleteCommunityMapShop")
    @RestReturn(value=String.class)
    public RestResponse deleteCommunityMapShop(DeleteCommunityMapShopCommand cmd) {

        communityMapService.deleteCommunityMapShop(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

}
