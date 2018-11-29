package com.everhomes.buttscript.admin;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.buttscript.ButtInfoTypeEventMappingService;
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
 * buttInfoTypeEventMapping  Controller
 * @author huanglm
 *
 */
@RestDoc(value="ButtEventMapping Admin controller", site="core")
@RestController
@RequestMapping("/admin/buttEventMapping")
public class ButtInfoTypeEventMappingAdminController extends ControllerBase {

    @Autowired
    private ButtInfoTypeEventMappingService buttInfoTypeEventMappingService;
    /**
     * <b>URL: /admin/buttEventMapping/findButtEventMapping</b>
     * <p>1)通过域空间查询分类信息接口</p>
     */
    @RequestMapping("findButtEventMapping")
    @RestReturn(value=ButtInfoTypeEventMappingResponse.class)
    public RestResponse findButtEventMapping(FindButtEventMappingCommand cmd ) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        ButtInfoTypeEventMappingResponse resultDTO = buttInfoTypeEventMappingService.findButtEventMapping(cmd);
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
    }

    /**
     * <b>URL: /admin/buttEventMapping/updateButtEventMapping</b>
     * <p>2)修改事件配置信息</p>
     */
    @RequestMapping("updateButtEventMapping")
    @RestReturn(value=String.class)
    public RestResponse updateButtEventMapping(UpdateButtEventMappingCommand cmd ) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        buttInfoTypeEventMappingService.updateButtEventMapping(cmd);
        RestResponse response = new RestResponse();
        setResponseSuccess(response);
        return response;
    }

    /**
     * <b>URL: /admin/buttEventMapping/addButtEventMapping</b>
     * <p>3)新增事件配置信息</p>
     */
    @RequestMapping("addButtEventMapping")
    @RestReturn(value=ButtInfoTypeEventMappingDTO.class)
    public RestResponse addButtEventMapping(AddButtEventMappingCommand cmd ) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        ButtInfoTypeEventMappingDTO resultDTO = buttInfoTypeEventMappingService.crteateButtEventMapping(cmd);
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
    }

    /**
     * <b>URL: /admin/buttEventMapping/deleteButtEventMapping</b>
     * <p>4)删除事件配置信息</p>
     */
    @RequestMapping("deleteButtEventMapping")
    @RestReturn(value=String.class)
    public RestResponse deleteButtEventMapping(DeleteButtEventMappingCommand cmd ) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        buttInfoTypeEventMappingService.deleteButtEventMapping(cmd);
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
