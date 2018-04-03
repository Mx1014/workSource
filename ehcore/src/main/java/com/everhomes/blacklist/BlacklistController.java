package com.everhomes.blacklist;


import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.blacklist.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestDoc(value="blacklist controller", site="core")
@RestController
@RequestMapping("/blacklist")
public class BlacklistController extends ControllerBase {

    @Autowired
    private BlacklistService blacklistService;

    /**
     * <b>URL: /blacklist/listUserBlacklists</b>
     * <p>黑名单列表</p>
     */
    @RequestMapping("listUserBlacklists")
    @RestReturn(value=ListUserBlacklistsResponse.class)
    public RestResponse listUserBlacklists(@Valid ListUserBlacklistsCommand cmd) {
        RestResponse response =  new RestResponse(blacklistService.listUserBlacklists(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /blacklist/checkUserBlacklist</b>
     * <p>校验用户是否在黑名单</p>
     */
    @RequestMapping("checkUserBlacklist")
    @RestReturn(value=UserBlacklistDTO.class)
    public RestResponse checkUserBlacklist(@Valid CheckUserBlacklistCommand cmd) {
        RestResponse response =  new RestResponse(blacklistService.checkUserBlacklist(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /blacklist/addUserBlacklist</b>
     * <p>添加黑名单</p>
     */
    @RequestMapping("addUserBlacklist")
    @RestReturn(value=UserBlacklistDTO.class)
    public RestResponse addUserBlacklist(@Valid AddUserBlacklistCommand cmd) {
        RestResponse response =  new RestResponse(blacklistService.addUserBlacklist(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /blacklist/editUserBlacklist</b>
     * <p>编辑黑名单</p>
     */
    @RequestMapping("editUserBlacklist")
    @RestReturn(value=String.class)
    public RestResponse editUserBlacklist(@Valid AddUserBlacklistCommand cmd) {
        blacklistService.editUserBlacklist(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /blacklist/batchDeleteUserBlacklist</b>
     * <p>批量解除黑名单</p>
     */
    @RequestMapping("batchDeleteUserBlacklist")
    @RestReturn(value=String.class)
    public RestResponse batchDeleteUserBlacklist(@Valid BatchDeleteUserBlacklistCommand cmd) {
        blacklistService.batchDeleteUserBlacklist(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /blacklist/listBlacklistPrivileges</b>
     * <p>黑名单权限列表</p>
     */
    @RequestMapping("listBlacklistPrivileges")
    @RestReturn(value=BlacklistPrivilegeDTO.class, collection = true)
    public RestResponse listBlacklistPrivileges() {
        RestResponse response =  new RestResponse(blacklistService.listBlacklistPrivileges());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /blacklist/listUserBlacklistPrivileges</b>
     * <p>用户黑名单权限列表</p>
     */
    @RequestMapping("listUserBlacklistPrivileges")
    @RestReturn(value=BlacklistPrivilegeDTO.class, collection = true)
    public RestResponse listUserBlacklistPrivileges(@Valid ListUserBlacklistPrivilegesCommand cmd) {
        RestResponse response =  new RestResponse(blacklistService.listUserBlacklistPrivileges(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
