package com.everhomes.launchpadbase;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.launchpad.LaunchPadService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.rest.launchpadbase.UpdateUserAppsCommand;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestDoc(value="LaunchPadBase controller", site="core")
@RestController
@RequestMapping("/launchpadbase")
public class LaunchPadBaseController extends ControllerBase {

    @Autowired
    private LaunchPadService launchPadService;
    @Autowired
    private ServiceModuleAppService serviceModuleAppService;

    /**
     * <b>URL: /launchpad/getLaunchPadLayout</b>
     * <p>根据id获取服务市场样式   </p>
     */
    @RequestMapping("getLaunchPadLayout")
    @RestReturn(value=LaunchPadLayoutDTO.class)
    @RequireAuthentication(false)
    public RestResponse getLaunchPadLayout(@Valid GetLaunchPadLayoutCommand cmd) {
        LaunchPadLayoutDTO dto = launchPadService.getLaunchPadBaseLayout(cmd);
        RestResponse response =  new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /launchpadbase/listBanners</b>
     * <p>获取banners</p>
     */
    @RequestMapping("listBanners")
    @RestReturn(value=ListBannersResponse.class)
    @RequireAuthentication(false)
    public RestResponse listBanners(ListBannersCommand cmd) {
        ListBannersResponse listRes = launchPadService.listBanners(cmd);
        RestResponse response =  new RestResponse(listRes);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpadbase/listLaunchPadApps</b>
     * <p>广场根据组件获取应用</p>
     */
    @RequestMapping("listLaunchPadApps")
    @RestReturn(value=ListLaunchPadAppsResponse.class)
    @RequireAuthentication(false)
    public RestResponse listLaunchPadApps(ListLaunchPadAppsCommand cmd) {
        ListLaunchPadAppsResponse res = serviceModuleAppService.listLaunchPadApps(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpadbase/updateUserApps</b>
     * <p>标准版编辑用户首页数据</p>
     */
    @RequestMapping("updateUserApps")
    @RestReturn(value=String.class)
    public RestResponse updateUserApps(UpdateUserAppsCommand cmd) {
        serviceModuleAppService.updateBaseUserApps(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpadbase/listAllLaunchPadApps</b>
     * <p>广场根据组件获取全部应用</p>
     */
    @RequestMapping("listAllLaunchPadApps")
    @RestReturn(value=ListAllLaunchPadAppsResponse.class)
    @RequireAuthentication(false)
    public RestResponse listAllLaunchPadApps(ListAllLaunchPadAppsCommand cmd) {
        ListAllLaunchPadAppsResponse res = serviceModuleAppService.listAllLaunchPadApps(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /launchpadbase/listAllApps</b>
     * <p>广场根据组件获取全部应用，包括自定义的</p>
     */
    @RequestMapping("listAllApps")
    @RestReturn(value=ListAllAppsResponse.class)
    @RequireAuthentication(false)
    public RestResponse listAllApps(ListAllLaunchPadAppsCommand cmd) {
        ListAllAppsResponse res = serviceModuleAppService.listAllApps(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpadbase/listOPPushCards</b>
     * <p>获取运营板块信息</p>
     */
    @RequestMapping("listOPPushCards")
    @RestReturn(value=ListOPPushCardsResponse.class)
    @RequireAuthentication(false)
    public RestResponse listOPPushCards(ListOPPushCardsCommand cmd) {
        ListOPPushCardsResponse res = launchPadService.listOPPushCards(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /launchpadbase/listBulletinsCards</b>
     * <p>获取公告板块信息</p>
     */
    @RequestMapping("listBulletinsCards")
    @RestReturn(value=ListBulletinsCardsResponse.class)
    @RequireAuthentication(false)
    public RestResponse listBulletinsCards(ListBulletinsCardsCommand cmd) {
        ListBulletinsCardsResponse res = launchPadService.listBulletinsCards(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpadbase/listWorkPlatformApps</b>
     * <p>获取工作台app</p>
     */
    @RequestMapping("listWorkPlatformApps")
    @RestReturn(value=ListWorkPlatformAppResponse.class)
    public RestResponse listWorkPlatformApps(ListWorkPlatformAppCommand cmd) {
        ListWorkPlatformAppResponse res = serviceModuleAppService.listWorkPlatformApp(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpadbase/saveWorkPlatformApps</b>
     * <p>保存工作台app设置</p>
     */
    @RequestMapping("saveWorkPlatformApps")
    @RestReturn(value=String.class)
    public RestResponse saveWorkPlatformApps(SaveWorkPlatformAppCommand cmd) {
        serviceModuleAppService.saveWorkPlatformApp(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpadbase/updateWorkPlatformApps</b>
     * <p>保存工作台app可见性设置</p>
     */
    @RequestMapping("updateWorkPlatformApps")
    @RestReturn(value=String.class)
    public RestResponse updateWorkPlatformApps(UpdateWorkPlatformAppCommand cmd) {
        serviceModuleAppService.updateWorkPlatformApp(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /launchpadbase/updateWorkPlatformAppsSort</b>
     * <p>保存工作台app排序设置</p>
     */
    @RequestMapping("updateWorkPlatformAppsSort")
    @RestReturn(value=String.class)
    public RestResponse updateWorkPlatformAppsSort(UpdateWorkPlatformAppSortCommand cmd) {
        serviceModuleAppService.updateWorkPlatformAppSort(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
