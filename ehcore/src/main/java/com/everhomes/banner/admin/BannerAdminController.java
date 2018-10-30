package com.everhomes.banner.admin;

import com.everhomes.banner.BannerService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestDoc(value = "Banner controller", site = "core")
@RestController
@RequestMapping("/admin/banner")
public class BannerAdminController extends ControllerBase {

    // private static final Logger LOGGER = LoggerFactory.getLogger(BannerAdminController.class);

    @Autowired
    private BannerService bannerService;

    /**
     * <b>URL: /admin/banner/createBanner</b>
     * <p>创建banner</p>
     */
    @RequestMapping("createBanner")
    @RestReturn(value = BannerDTO.class)
    public RestResponse createBanner(@Valid CreateBannerCommand cmd) {
        bannerService.createBanner(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/banner/updateBanner</b>
     * <p>更新 banner</p>
     */
    @RequestMapping("updateBanner")
    @RestReturn(value = BannerDTO.class)
    public RestResponse updateBanner(@Valid UpdateBannerCommand cmd) {
        BannerDTO dto = bannerService.updateBanner(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/banner/deleteBanner</b>
     * <p>删除 banner</p>
     */
    @RequestMapping("deleteBanner")
    @RestReturn(value = String.class)
    public RestResponse deleteBanner(@Valid DeleteBannerCommand cmd) {
        bannerService.deleteBannerById(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/banner/listBanners</b>
     * <p>获取banner列表</p>
     */
    @RequestMapping("listBanners")
    @RestReturn(value = ListBannersResponse.class)
    public RestResponse listBanners(ListBannersCommand cmd) {
        ListBannersResponse result = bannerService.listBanners(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/banner/countEnabledBannersByScope</b>
     * <p>获取开启的banner数量，按照小区列出来</p>
     */
    @RequestMapping("countEnabledBannersByScope")
    @RestReturn(value = CountEnabledBannersByScopeResponse.class)
    public RestResponse countEnabledBannersByScope(CountEnabledBannersByScopeCommand cmd) {
        CountEnabledBannersByScopeResponse res = bannerService.countEnabledBannersByScope(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/banner/reorderBanners</b>
     * <p>调整banner的顺序</p>
     */
    @RequestMapping("reorderBanners")
    @RestReturn(value = String.class)
    public RestResponse reorderBanners(@Valid ReorderBannersCommand cmd) {
        bannerService.reorderBanners(cmd);
        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /admin/banner/updateBannerStatus</b>
     * <p>banner的开关</p>
     */
    @RequestMapping("updateBannerStatus")
    @RestReturn(value = BannerDTO.class)
    public RestResponse updateBannerStatus(@Valid UpdateBannerStatusCommand cmd) {
        BannerDTO dto = bannerService.updateBannerStatus(cmd);
        RestResponse resp = new RestResponse(dto);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /admin/banner/getBannerInstanconfig</b>
     * <p>获取banner配置</p>
     */
    @RequestMapping("getBannerInstanconfig")
    @RestReturn(value = BannerInstanconfigDTO.class)
    public RestResponse getBannerInstanconfig(@Valid GetBannerInstanconfigCommand cmd) {
        BannerInstanconfigDTO dto = bannerService.getBannerInstanconfig(cmd);
        RestResponse resp = new RestResponse(dto);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
}
