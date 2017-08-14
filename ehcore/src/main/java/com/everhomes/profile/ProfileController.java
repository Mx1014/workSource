package com.everhomes.profile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.profile.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2017.08.11
 * 1.通讯录与人事现在合并为同一块内容
 * 2.将人事从组织架构模块分离出来
 */

@RestController
@RequestMapping("/profile")
public class ProfileController extends ControllerBase{

    @Autowired
    private ProfileService profileService;

    /**
     * <b>URL: /profile/addProfileContacts</b>
     * <p>1.添加成员至通讯录</p>
     */
    @RequestMapping("addProfileContacts")
    @RestReturn(value = String.class)
    public RestResponse addProfileContacts(AddProfileContactsCommand cmd){
        AddProfileContactsResponse res = this.profileService.addProfileContacts(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/changeProfileContacts</b>
     */

    /**
     * <b>URL: /profile/deleteProfileContacts</b>
     * <p>2.删除通讯录成员</p>
     */
    @RequestMapping("deleteProfileContacts")
    @RestReturn(value = String.class)
    public RestResponse deleteProfileContacts(DeleteProfileContactsCommand cmd){
        this.profileService.deleteProfileContacts(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/stickProfileContacts</b>
     * <p>3.通讯录成员置顶</p>
     */
    @RequestMapping("stickProfileContacts")
    @RestReturn(value = String.class)
    public RestResponse stickProfileContacts(StickProfileContactsCommand cmd){
        this.profileService.stickProfileContacts(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/listProfileContacts</b>
     * <p>4.通讯录成员列表</p>
     */
    @RequestMapping("listProfileContacts")
    @RestReturn(value = String.class)
    public RestResponse listProfileContacts(listProfileContactsCommand cmd){
        listProfileContactsResponse res = this.profileService.listProfileContacts(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
