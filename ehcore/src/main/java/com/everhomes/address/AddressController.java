package com.everhomes.address;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.*;
import com.everhomes.rest.community.CommunityDoc;
import com.everhomes.rest.community.ListApartmentEnterpriseCustomersCommand;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.openapi.UserServiceAddressDTO;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.user.UserContext;
import com.everhomes.util.EtagHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

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
        Tuple<Integer, List<ApartmentDTO>> results = this.addressService.listApartmentsByKeywordNew(cmd);
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
     *
     * <b>URL: /address/betchDisclaimAddress</b>
     * <p>批量删除门牌地址（标准版）</p>
     * @param cmd
     * @return
     */
    @RequestMapping("/betchDisclaimAddress")
    @RestReturn(value = String.class)
    public RestResponse betchDisclaimAddress(BetchDisclaimAddressCommand cmd){
        //批量删除门牌地址
        addressService.betchDisclaimAddress(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /address/searchCommunities</b>
     * <p>搜索小区</p>
     */
    @RequestMapping("searchCommunities")
    @RestReturn(value=CommunityDoc.class, collection=true)
    public RestResponse searchCommunities(SearchCommunityCommand cmd) {
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
     * <b>URL: /address/getApartmentNameByBuildingName</b>
     * <p>根据域名，小区id，楼栋名查询门牌名和地址id的集合</p>
     */
    @RequestMapping("getApartmentNameByBuildingName")
    @RestReturn(value = GetApartmentNameByBuildingNameDTO.class,collection = true)
    public RestResponse getApartmentNameByBuildingName(GetApartmentNameByBuildingNameCommand cmd){
        List<GetApartmentNameByBuildingNameDTO> result = this.addressService.getApartmentNameByBuildingName(cmd);
        RestResponse resp = new RestResponse(result);
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

    /**
     * <b>URL: /address/uploadApartmentAttachment</b>
     * <p>上传门牌附件</p>
     */
    @RequestMapping("uploadApartmentAttachment")
    @RestReturn(value=ApartmentAttachmentDTO.class)
    public RestResponse uploadApartmentAttachment(@Valid UploadApartmentAttachmentCommand cmd) {
        ApartmentAttachmentDTO dto = addressService.uploadApartmentAttachment(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/deleteApartmentAttachment</b>
     * <p>删除附件</p>
     */
    @RequestMapping("deleteApartmentAttachment")
    @RestReturn(value= java.lang.String.class)
    public RestResponse deleteApartmentAttachment(@Valid DeleteApartmentAttachmentCommand cmd) {
        addressService.deleteApartmentAttachment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /activity/downloadApartmentAttachment</b>
     * <p> 下载门牌附件 </p>
     */
    @RequireAuthentication(false)
    @RequestMapping("downloadApartmentAttachment")
    @RestReturn(value = String.class)
    public RestResponse downloadApartmentAttachment(DownloadApartmentAttachmentCommand cmd) {
        addressService.downloadApartmentAttachment(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/listApartmentAttachments</b>
     * <p>查询门牌的所有附件列表</p>
     */
    @RequestMapping("listApartmentAttachments")
    @RestReturn(value=ApartmentAttachmentDTO.class, collection = true)
    public RestResponse listApartmentAttachments(@Valid ListApartmentAttachmentsCommand cmd) {
        List<ApartmentAttachmentDTO> dtos = addressService.listApartmentAttachments(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/listApartmentEnterpriseCustomers</b>
     * <p>查询门牌的关联的企业</p>
     */
    @RequestMapping("listApartmentEnterpriseCustomers")
    @RestReturn(value=EnterpriseCustomerDTO.class, collection = true)
    public RestResponse listApartmentEnterpriseCustomers(@Valid ListApartmentEnterpriseCustomersCommand cmd) {
        List<EnterpriseCustomerDTO> dtos = addressService.listApartmentEnterpriseCustomers(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/createOfficeSite/<b>
     * <p>添加办公地点</p>
     */
    @RequestMapping("createOfficeSite")
    @RestReturn(value=String.class)
    public RestResponse createOfficeSite(@Valid CreateOfficeSiteCommand cmd) {
//        List<EnterpriseCustomerDTO> dtos = addressService.listApartmentEnterpriseCustomers(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/createAddressArrangement</b>
     * <p>创建房源相关的拆分/合并计划</p>
     */
    @RequestMapping("createAddressArrangement")
    @RestReturn(value = String.class)
    public RestResponse createAddressArrangement(CreateAddressArrangementCommand cmd) {
    	addressService.createAddressArrangement(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/listAddressArrangement</b>
     * <p>查看房源相关的拆分/合并计划</p>
     */
    @RequestMapping("listAddressArrangement")
    @RestReturn(value=AddressArrangementDTO.class)
    public RestResponse listAddressArrangement(ListAddressArrangementCommand cmd) {
    	AddressArrangementDTO dto = addressService.listAddressArrangement(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/updateAddressArrangement</b>
     * <p>修改房源相关的拆分/合并计划</p>
     */
    @RequestMapping("updateAddressArrangement")
    @RestReturn(value = String.class)
    public RestResponse updateAddressArrangement(UpdateAddressArrangementCommand cmd) {
    	addressService.updateAddressArrangement(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/deleteAddressArrangement</b>
     * <p>删除房源相关的拆分/合并计划</p>
     */
    @RequestMapping("deleteAddressArrangement")
    @RestReturn(value = String.class)
    public RestResponse deleteAddressArrangement(DeleteAddressArrangementCommand cmd) {
    	addressService.deleteAddressArrangement(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/getHistoryApartment</b>
     * <p>查找历史房源</p>
     */
    @RequestMapping("getHistoryApartment")
    @RestReturn(value=HistoryApartmentDTO.class, collection = true)
    public RestResponse getHistoryApartment(GetHistoryApartmentCommand cmd) {
    	List<HistoryApartmentDTO> dtos=addressService.getHistoryApartment(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/excuteAddressArrangement</b>
     * <p>测试接口：用于执行拆分合并计划</p>
     */
    @RequestMapping("excuteAddressArrangement")
    @RestReturn(value=String.class)
    public RestResponse excuteAddressArrangement(ExcuteAddressArrangementCommand cmd) {
    	addressService.excuteAddressArrangement(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /address/fixInvalidCityInAddresses</b>
     * <p>修复地址中的城市ID</p>
     */
    @RequestMapping("fixInvalidCityInAddresses")
    @RestReturn(value=String.class)
    public RestResponse fixInvalidCityInAddresses(FixInvalidCityInAddressCommand cmd) {
        addressService.fixInvalidCityInAddresses(cmd.getNamespaceId());
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
