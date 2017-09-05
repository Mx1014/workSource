package com.everhomes.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.address.*;
import com.everhomes.rest.enterprise.SearchEnterpriseCommunityCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.community.CommunityDoc;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.openapi.UserServiceAddressDTO;
import com.everhomes.rest.ui.organization.ListCommunitiesBySceneResponse;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.user.UserContext;
import com.everhomes.util.EtagHelper;
import com.everhomes.util.Tuple;

@RestController
@RequestMapping("/address")
public class AddressController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);
    
    @Autowired
    private AddressService addressService;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private CommunitySearcher searcher;

    /**
     * <b>URL: /address/suggestCommunity</b>
     * <p>添加小区</p>
     */
    @RequestMapping("suggestCommunity")
    @RestReturn(value=SuggestCommunityDTO.class)
    public RestResponse addCommunity(@Valid SuggestCommunityCommand cmd) {
        SuggestCommunityDTO result = this.addressService.suggestCommunity(cmd);
        RestResponse response =  new RestResponse(result);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/listSuggestedCommunities</b>
     * <p>查询用户创建的小区</p>
     */
    @RequestMapping("listSuggestedCommunities")
    @RestReturn(value=CommunitySummaryDTO.class, collection=true)
    public RestResponse listSuggestedCommunities() {
        Tuple<Integer, List<CommunitySummaryDTO>> result = this.addressService.listSuggestedCommunities();
        RestResponse response =  new RestResponse(result.second());

        response.setErrorCode(result.first());
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/listNearbyCommunities</b>
     * <p>根据经纬度查询附近小区</p>
     */
    @RequestMapping("listNearbyCommunities")
    @RestReturn(value=CommunityDTO.class, collection=true)
    public RestResponse listNearbyCommunities(@Valid ListNearbyCommunityCommand cmd) {
        Tuple<Integer, List<CommunityDTO>> results = this.addressService.listNearbyCommunities(cmd);
        RestResponse response =  new RestResponse(results.second());

        response.setErrorCode(results.first());
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/listCommunitiesByKeyword</b>
     * <p>根据城市Id和关键字查询小区</p>
     */
    @RequestMapping("listCommunitiesByKeyword")
    @RestReturn(value=CommunityDTO.class, collection=true)
    public RestResponse listCommunitiesByKeyword(@Valid ListCommunityByKeywordCommand cmd) {
        Tuple<Integer, List<CommunityDTO>> results = this.addressService.listCommunitiesByKeyword(cmd);
        RestResponse response = new RestResponse(results.second());

        response.setErrorCode(results.first());
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/listBuildingsByKeyword</b>
     * <p>根据小区Id和关键字查询小区楼栋</p>
     */
    @RequestMapping("listBuildingsByKeyword")
    @RestReturn(value=BuildingDTO.class, collection=true)
    public RestResponse listBuildingsByKeyword(@Valid ListBuildingByKeywordCommand cmd) {
        Tuple<Integer, List<BuildingDTO>> results = this.addressService.listBuildingsByKeyword(cmd);
        RestResponse response = new RestResponse(results.second());
        
        response.setErrorCode(results.first());
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/listApartmentsByKeyword</b>
     * <p>根据小区Id、楼栋号和关键字查询门牌</p>
     */
    @RequestMapping("listApartmentsByKeyword")
    @RestReturn(value=ApartmentDTO.class, collection=true)
    public RestResponse listApartmentsByKeyword(@Valid ListPropApartmentsByKeywordCommand cmd) {
        Tuple<Integer, List<ApartmentDTO>> results = this.addressService.listApartmentsByKeyword(cmd);
        RestResponse response = new RestResponse(results.second());
        
        response.setErrorCode(results.first());
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/claimAddress</b>
     * <p>添加地址或修改地址</p>
     */
    @RequestMapping("claimAddress")
    @RestReturn(value=ClaimedAddressInfo.class)
    public RestResponse claimAddress(@Valid ClaimAddressCommand cmd) {
        ClaimedAddressInfo info = this.addressService.claimAddress(cmd);
        RestResponse response = new RestResponse(info);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/claimAddressV2</b>
     * <p>添加地址或修改地址</p>
     */
    @RequestMapping("claimAddressV2")
    @RestReturn(value=FamilyDTO.class)
    public RestResponse claimAddressV2(@Valid ClaimAddressCommand cmd) {
        FamilyDTO info = this.addressService.claimAddressV2(cmd);
        RestResponse response = new RestResponse(info);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/disclaimAddress</b>
     * <p>删除地址</p>
     */
    @RequestMapping("disclaimAddress")
    @RestReturn(value=String.class)
    public RestResponse disclaimAddress(@Valid DisclaimAddressCommand cmd) {
        this.addressService.disclaimAddress(cmd);
        RestResponse response = new RestResponse(null);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/searchCommunities</b>
     * <p>根据关键字搜索小区</p>
     */
    @RequestMapping("searchCommunities")
    @RestReturn(value=CommunityDoc.class, collection=true)
    public RestResponse searchCommunities(@Valid SearchCommunityCommand cmd) {
        List<CommunityDoc> results = this.addressService.searchCommunities(cmd);
        RestResponse response =  new RestResponse(results);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/findNearyCommunityById</b>
     * <p>根据小区Id查询附近小区</p>
     */
    @RequestMapping("findNearyCommunityById")
    @RestReturn(value=CommunityDoc.class, collection=true)
    public RestResponse findNearyCommunityById(@Valid ListPropApartmentsByKeywordCommand cmd) {
        List<Community> results = this.communityProvider.findNearyByCommunityById(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), cmd.getCommunityId());
        RestResponse response =  new RestResponse(results);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/listAddressByKeyword</b>
     * <p>根据关键字查询地址</p>
     */
    @RequestMapping("listAddressByKeyword")
    @RestReturn(value=ListAddressByKeywordCommandResponse.class)
    public RestResponse listAddressByKeyword(ListAddressByKeywordCommand cmd) {
        
        ListAddressByKeywordCommandResponse results = this.addressService.listAddressByKeyword(cmd);
        RestResponse response =  new RestResponse(results) ;
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/listApartmentsByBuildingName</b>
     * <p>根据小区Id、楼栋号查询门牌列表</p>
     */
    @RequestMapping("listApartmentsByBuildingName")
    @RestReturn(value=ListApartmentByBuildingNameCommandResponse.class)
    public RestResponse listApartmentsByBuildingName(@Valid ListApartmentByBuildingNameCommand cmd,HttpServletRequest request,HttpServletResponse response) {
        ListApartmentByBuildingNameCommandResponse result = this.addressService.listApartmentsByBuildingName(cmd);
        RestResponse resp = new RestResponse();
        if(EtagHelper.checkHeaderEtagOnly(30,result.hashCode()+"", request, response)) {
            resp.setResponseObject(result);
        }
        
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /address/getApartmentByBuildingApartmentName</b>
     * <p>查询门牌</p>
     */
    @RequestMapping("getApartmentByBuildingApartmentName")
    @RestReturn(value=String.class)
    public RestResponse getApartmentByBuildingApartmentName(GetApartmentByBuildingApartmentNameCommand cmd) {
    	AddressDTO dto = addressService.getApartmentByBuildingApartmentName(cmd);
        RestResponse resp = new RestResponse(dto);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /address/createServiceAddress</b>
     * <p>创建服务地址</p>
     */
    @RequestMapping("createServiceAddress")
    @RestReturn(value=UserServiceAddressDTO.class)
    public RestResponse createServiceAddress(@Valid CreateServiceAddressCommand cmd) {
        UserServiceAddressDTO addressDTO = this.addressService.createServiceAddress(cmd);
        
        RestResponse response = new RestResponse(addressDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/deleteServiceAddress</b>
     * <p>删除服务地址</p>
     */
    @RequestMapping("deleteServiceAddress")
    @RestReturn(value=String.class)
    public RestResponse deleteServiceAddress(@Valid DeleteServiceAddressCommand cmd) {
        this.addressService.deleteServiceAddress(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /address/listUnassignedApartmentsByBuildingName</b>
     * <p>根据小区Id、楼栋号查询未入驻的门牌列表</p>
     */
    @RequestMapping("listUnassignedApartmentsByBuildingName")
    @RestReturn(value=ApartmentDTO.class, collection = true)
    public RestResponse listUnassignedApartmentsByBuildingName(@Valid ListApartmentByBuildingNameCommand cmd,HttpServletRequest request,HttpServletResponse response) {
    	List<ApartmentDTO> result = this.addressService.listUnassignedApartmentsByBuildingName(cmd);
        RestResponse resp = new RestResponse();
        if(EtagHelper.checkHeaderEtagOnly(30,result.hashCode()+"", request, response)) {
            resp.setResponseObject(result);
        }
        
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /address/listNearbyMixCommunities</b>
     * <p>获取附近混合园区/小区列表</p>
     */
    @RequestMapping("listNearbyMixCommunities")
    @RestReturn(value=ListNearbyMixCommunitiesCommandResponse.class)
    public RestResponse listNearbyMixCommunities(@Valid ListNearbyMixCommunitiesCommand cmd) {
 		
    	ListNearbyMixCommunitiesCommandResponse res = addressService.listNearbyMixCommunities(cmd);

 		RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/listCommunityApartmentsByBuildingName</b>
     * <p>根据小区Id、楼栋号查询门牌列表(因为listApartmentsByBuildingName里面check了用户权限所以要在提供一个)</p>
     */
    @RequestMapping("listCommunityApartmentsByBuildingName")
    @RestReturn(value=ListApartmentByBuildingNameCommandResponse.class)
    public RestResponse listCommunityApartmentsByBuildingName(@Valid ListApartmentByBuildingNameCommand cmd,HttpServletRequest request,HttpServletResponse response) {
        ListApartmentByBuildingNameCommandResponse result = this.addressService.listCommunityApartmentsByBuildingName(cmd);
        RestResponse resp = new RestResponse();
        if(EtagHelper.checkHeaderEtagOnly(30,result.hashCode()+"", request, response)) {
            resp.setResponseObject(result);
        }

        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /address/listNearbyMixCommunitiesV2</b>
     * <p>选择附近的社区列表</p>
     */
    @RequestMapping("listNearbyMixCommunitiesV2")
    @RestReturn(value=ListNearbyMixCommunitiesCommandV2Response.class)
    public RestResponse listNearbyMixCommunitiesV2(@Valid ListNearbyMixCommunitiesCommand cmd) {
        ListNearbyMixCommunitiesCommandV2Response res = addressService.listNearbyMixCommunitiesV2(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/listPopularCommunitiesWithType</b>
     * <p>选择热门社区列表</p>
     */
    @RequestMapping("listPopularCommunitiesWithType")
    @RestReturn(value=ListNearbyMixCommunitiesCommandV2Response.class)
    public RestResponse listPopularCommunitiesWithType(@Valid ListNearbyMixCommunitiesCommand cmd) {
        ListNearbyMixCommunitiesCommandV2Response res = addressService.listPopularCommunitiesWithType(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
