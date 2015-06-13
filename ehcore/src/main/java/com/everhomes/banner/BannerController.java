package com.everhomes.banner;



import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestDoc(value="Banner controller", site="core")
@RestController
@RequestMapping("/banner")
public class BannerController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(BannerController.class);
    
    @Autowired
    private BannerService bannerService;

    /**
     * <b>URL: /banner/listBannerByCommunityId</b>
     * <p>获取用户相关的banner</p>
     */
    @RequestMapping("listBannerByCommunityId")
    @RestReturn(value=BannerDTO.class,collection=true)
    public RestResponse listLaunchPadByCommunityId(@Valid ListBannerByCommunityIdCommand cmd) {
        
        List<BannerDTO> result = bannerService.listBannerByCommuniyId(cmd);
        RestResponse response =  new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /banner/createBanner</b>
     * <p>创建banner</p>
     */
    @RequestMapping("createBanner")
    @RestReturn(value=String.class)
    public RestResponse createBanner(@Valid CreateBannerCommand cmd) {
        
        bannerService.createBanner(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /banner/updateBanner</b>
     * <p>更新banner</p>
     */
    @RequestMapping("updateBanner")
    @RestReturn(value=String.class)
    public RestResponse updateBanner(@Valid UpdateBannerCommand cmd) {
        
        bannerService.updateBanner(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /banner/deleteBanner</b>
     * <p>删除banner</p>
     */
    @RequestMapping("deleteBanner")
    @RestReturn(value=String.class)
    public RestResponse deleteBanner(@Valid DeleteBannerCommand cmd) {
        
        bannerService.deleteBannerById(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /banner/clickBanner</b>
     * <p>点击banner</p>
     */
    @RequestMapping("clickBanner")
    @RestReturn(value=String.class)
    public RestResponse clickBanner(@Valid CreateBannerClickCommand cmd) {
        
        String token = bannerService.createOrUpdateBannerClick(cmd);
        RestResponse response =  new RestResponse(token);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
