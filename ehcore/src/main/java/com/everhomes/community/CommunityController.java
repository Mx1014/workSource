package com.everhomes.community;



import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.address.CommunityDTO;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestDoc(value="Community controller", site="core")
@RestController
@RequestMapping("/community")
public class CommunityController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityController.class);
    
    @Autowired
    private CommunityService communityService;
    
    
    /**
     * <b>URL: /community/findCommunitiesByNameAndCityId</b>
     * <p>根据小区名称和城市id搜索小区</p>
     */
    @RequestMapping("findCommunitiesByNameAndCityId")
    @RestReturn(value=CommunityDTO.class, collection=true)
    public RestResponse findCommunitiesByNameAndCityId(@Valid FindCommunitiesByNameAndCityIdCommand cmd) {
        List<CommunityDTO> results = this.communityService.findCommunitiesByNameAndCityId(cmd);

        RestResponse response =  new RestResponse(results);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /community/findCommunitiesByIds</b>
     * <p>根据小区id列表查询小区列表</p>
     */
    @RequestMapping("findCommunitiesByIds")
    @RestReturn(value=CommunityDTO.class, collection=true)
    public RestResponse findCommunitiesByIds(@Valid FindCommunitiesByIdsCommand cmd) {
        List<CommunityDTO> results = this.communityService.findCommunitiesByIds(cmd);
        RestResponse response =  new RestResponse(results);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/updateCommunityRequestStatus</b>
     * <p>根据小区id更新小区收集状态</p>
     */
    @RequestMapping("updateCommunityRequestStatus")
    @RestReturn(value=String.class)
    public RestResponse updateCommunityRequestStatus(@Valid UpdateCommunityRequestStatusCommand cmd) {
        this.communityService.updateCommunityRequestStatus(cmd);
        RestResponse response =  new RestResponse();

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
