package com.everhomes.buttscript.admin;


import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.buttscript.ButtScriptConfigService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.buttscript.*;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * buttScriptConfig  Controller
 * @author huanglm
 *
 */
@RestDoc(value="ButtScriptConfig Admin controller", site="core")
@RestController
@RequestMapping("/admin/buttScriptConfig")
public class ButtScriptConfigAdminController extends ControllerBase {

    @Autowired
    private ButtScriptConfigService buttScriptConfigService ;

    /**
     * <b>URL: /admin/buttScriptConfig/findButtScriptConfing</b>
     * <p>1)通过域空间查询分类信息接口</p>
     */
    @RequestMapping("findButtScriptConfing")
    @RestReturn(value=ButtScriptConfingResponse.class)
    public RestResponse findButtScriptConfing(FindButtScriptConfingCommand cmd ) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        ButtScriptConfingResponse resultDTO = buttScriptConfigService.findButtScriptConfigByNamespaceId(cmd);
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
    }

    /**
     * <b>URL: /admin/buttScriptConfig/updateButtScriptConfing</b>
     * <p>2)修改分类信息接口</p>
     */
    @RequestMapping("updateButtScriptConfing")
    @RestReturn(value=String.class)
    public RestResponse updateButtScriptConfing(UpdateButtScriptConfingCommand cmd ) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        buttScriptConfigService.updateButtScriptConfing(cmd);
        RestResponse response = new RestResponse();
        setResponseSuccess(response);
        return response;
    }

    /**
     * <b>URL: /admin/buttScriptConfig/addButtScriptConfing</b>
     * <p>3)新增分类信息接口</p>
     */
    @RequestMapping("addButtScriptConfing")
    @RestReturn(value=ButtScriptConfigDTO.class)
    public RestResponse addButtScriptConfing(AddButtScriptConfingCommand cmd ) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        ButtScriptConfigDTO resultDTO = buttScriptConfigService.crteateButtScriptConfing(cmd);
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
    }

    /**
     * <b>URL: /admin/buttScriptConfig/updateButtScriptConfingStatus</b>
     * <p>4)配置失效接口/配置生效接口</p>
     */
    @RequestMapping("updateButtScriptConfingStatus")
    @RestReturn(value=String.class)
    public RestResponse updateButtScriptConfingStatus(UpdateButtScriptConfingStatusCommand cmd ) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        buttScriptConfigService.updateButtScriptConfingStatus(cmd);
        RestResponse response = new RestResponse();
        setResponseSuccess(response);
        return response;
    }

    /**
     * <p>设置response 成功信息</p>
     * @param response
     */
    private void setResponseSuccess(RestResponse response){
        if(response == null ) return ;

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
    }
}
