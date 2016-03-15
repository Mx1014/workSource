package com.everhomes.banner;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.CreateBannerClickCommand;
import com.everhomes.rest.banner.GetBannerByIdCommand;
import com.everhomes.rest.banner.GetBannersCommand;
import com.everhomes.util.EtagHelper;

@RestDoc(value="Banner controller", site="core")
@RestController
@RequestMapping("/banner")
public class BannerController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(BannerController.class);
    private static final String MARKETDATA_ITEM_VERSION = "marketdata.item.version";
    @Autowired
    private BannerService bannerService;
    @Autowired
    private ConfigurationProvider configurationProvider;

    /**
     * <b>URL: /banner/getBanners</b>
     * <p>获取用户相关的banner</p>
     */
    @RequestMapping("getBanners")
    @RestReturn(value=BannerDTO.class,collection=true)
    public RestResponse getBanners(@Valid GetBannersCommand cmd,HttpServletRequest request,HttpServletResponse response) {
        
        List<BannerDTO> result = bannerService.getBanners(cmd,request);
        RestResponse resp =  new RestResponse();
        int hashCode = configurationProvider.getIntValue(MARKETDATA_ITEM_VERSION, 0);
        if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
            resp.setResponseObject(result);
        }
        
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    @RequestMapping("getBannerByIdCommand")
    @RestReturn(value=BannerDTO.class)
    public RestResponse getBannerById(@Valid GetBannerByIdCommand cmd){
        BannerDTO bannerDto = bannerService.getBannerById(cmd);
        RestResponse response = new RestResponse(bannerDto);
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
