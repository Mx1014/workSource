package com.everhomes.app;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.app.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/appkey")
public class AppController extends ControllerBase {
    
    // TODO, we need to maintain the trust list in database
    private static Set<String> s_trustedApps = new HashSet<>();
    
    static {
        s_trustedApps.add(AppConstants.APPKEY_BORDER);
        s_trustedApps.add(AppConstants.APPKEY_BIZ);
        s_trustedApps.add(AppConstants.APPKEY_APP);
    }

    @Autowired
    private AppService appService;
    
    @RequestMapping("isTrustedApp")
    @RestReturn(value=String.class)
    public RestResponse isTrustedApp(@Valid TrustedAppCommand cmd) {
        String appKey = cmd.getThirdPartyAppKey();
        if (appKey == null) {
            appKey = cmd.getAppKey();
        }

        if (s_trustedApps.contains(appKey)) {
            return new RestResponse("YES");
        }

        App app = appService.find(appKey);
        if (app != null && app.getName() != null) {
            if (app.getName().trim().startsWith("zuolin")) {
                return new RestResponse("YES");
            }
        }

        // 之前是否授权过
        boolean isGranted = appService.isGrantedApp(app, UserContext.currentUserId());
        if (isGranted) {
            return new RestResponse("YES");
        }
        return new RestResponse("NO");
    }

    /**
     * <p>根据appkey获取app</p>
     * <b>/appkey/findApp</b>
     * @param cmd
     * @return
     */
    @RequestMapping("findApp")
    @RestReturn(value=AppDTO.class)
    public RestResponse findApp(@Valid GetAppCommand cmd) {

        App app = appService.find(cmd.getRealAppKey());

        return new RestResponse(ConvertHelper.convert(app, AppDTO.class));
    }

    /**
     * <p>创建App</p>
     * <b>URL: /appkey/createApp</b>
     */
    @RequestMapping("createApp")
    @RestReturn(value=AppDTO.class)
    public RestResponse createApp(@Valid CreateAppCommand cmd) {
        AppDTO dto = appService.createApp(cmd);
        return new RestResponse(dto);
    }

    /**
     * <p>删除App</p>
     * <b>URL: /appkey/deleteApp</b>
     */
    @RequestMapping("deleteApp")
    @RestReturn(value=String.class)
    public RestResponse deleteApp(@Valid DeleteAppCommand cmd) {
        appService.deleteApp(cmd);
        return new RestResponse();
    }

    /**
     * <p>App列表</p>
     * <b>URL: /appkey/listApps</b>
     */
    @RequestMapping("listApps")
    @RestReturn(value=ListAppsResponse.class)
    public RestResponse listApps(@Valid ListAppsCommand cmd) {
        ListAppsResponse response = appService.listApps(cmd);
        return new RestResponse(response);
    }

    @RequestMapping("isValidAppUrlBinding")
    @RestReturn(value=String.class)
    public RestResponse isValidAppUrlBinding(@Valid VerifyAppUrlBindingCommand cmd) {
        return new RestResponse("YES");
    }
}
