package com.everhomes.address;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityDoc;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.family.FamilyDTO;
import com.everhomes.rest.RestResponse;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.util.ConvertHelper;
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
        List<Community> results = this.communityProvider.findNearyByCommunityById(cmd.getCommunityId());
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
     * <b>URL: /address/createServiceAddress</b>
     * <p>创建服务地址</p>
     */
    @RequestMapping("createServiceAddress")
    @RestReturn(value=AddressDTO.class)
    public RestResponse createServiceAddress(@Valid CreateServiceAddressCommand cmd) {
        AddressDTO addressDTO = this.addressService.createServiceAddress(cmd);
        
        RestResponse response = new RestResponse(addressDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
