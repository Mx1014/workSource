package com.everhomes.banner.admin;

import com.everhomes.banner.BannerService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.GetBannerByIdCommand;
import com.everhomes.rest.banner.admin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestDoc(value = "Banner controller", site = "core")
@RestController
@RequestMapping("/admin/banner")
public class BannerAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(BannerAdminController.class);

    @Autowired
    private BannerService bannerService;

    /**
     * <b>URL: /admin/banner/createBanner</b>
     * <p>创建banner</p>
     */
    @RequestMapping("createBanner")
    @RestReturn(value = String.class)
    public RestResponse createBanner(@Valid CreateBannerAdminCommand cmd) {

        // SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        // resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        bannerService.createBanner(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/banner/getBannerById</b>
     * <p>根据bannerId获取banner信息</p>
     */
    @RequestMapping("getBannerById")
    @RestReturn(value = BannerDTO.class)
    public RestResponse getBannerById(@Valid GetBannerByIdCommand cmd) {
        BannerDTO bannerDto = bannerService.getBannerById(cmd);
        RestResponse response = new RestResponse(bannerDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/banner/updateBanner</b>
     * <p>更新banner</p>
     */
    @RequestMapping("updateBanner")
    @RestReturn(value = String.class)
    public RestResponse updateBanner(@Valid UpdateBannerAdminCommand cmd) {

        bannerService.updateBanner(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/banner/deleteBanner</b>
     * <p>删除banner</p>
     */
    @RequestMapping("deleteBanner")
    @RestReturn(value = String.class)
    public RestResponse deleteBanner(@Valid DeleteBannerAdminCommand cmd) {

        bannerService.deleteBannerById(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/banner/listBanners</b>
     * <p>获取所有banner</p>
     */
    @RequestMapping("listBanners")
    @RestReturn(value = ListBannersAdminCommandResponse.class)
    public RestResponse listBanners(ListBannersAdminCommand cmd) {
        ListBannersAdminCommandResponse result = bannerService.listBanners(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
