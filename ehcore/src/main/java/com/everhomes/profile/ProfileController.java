package com.everhomes.profile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.profile.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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
     * <p>1.添加、编辑通讯录成员</p>
     */
    @RequestMapping("addProfileContacts")
    @RestReturn(value = AddProfileContactsResponse.class)
    public RestResponse addProfileContacts(AddProfileContactsCommand cmd){
        AddProfileContactsResponse res = profileService.addProfileContacts(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/transferProfileContacts</b>
     * <p>2-1.调整通讯录成员</p>
     */
    @RequestMapping("transferProfileContacts")
    @RestReturn(value = String.class)
    public RestResponse transferProfileContacts(TransferProfileContactsCommand cmd){
        profileService.transferProfileContacts(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/deleteProfileContacts</b>
     * <p>2.删除通讯录成员</p>
     */
    @RequestMapping("deleteProfileContacts")
    @RestReturn(value = String.class)
    public RestResponse deleteProfileContacts(DeleteProfileContactsCommand cmd){
        profileService.deleteProfileContacts(cmd);
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
        profileService.stickProfileContacts(cmd);
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
    @RestReturn(value = ListProfileContactsResponse.class)
    public RestResponse listProfileContacts(ListProfileContactsCommand cmd){
        ListProfileContactsResponse res = profileService.listProfileContacts(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/importProfileContacts</b>
     * <p>5-1.导入通讯录成员</p>
     */
    @RequestMapping("importProfileContacts")
    @RestReturn(value = String.class)
    public RestResponse importProfileContacts(ImportProfileContactsCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
        User user = UserContext.current().getUser();
        RestResponse response = new RestResponse(profileService.importProfileContacts(files[0], user.getId(),user.getNamespaceId(),cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/exportProfileContacts</b>
     * <p>5-2.导出通讯录成员</p>
     */
    @RequestMapping("exportProfileContacts")
    @RestReturn(value = String.class)
    public RestResponse exportProfileContacts(ExportProfileContactsCommand cmd, HttpServletResponse httpResponse){
        profileService.exportProfileContacts(cmd,httpResponse);
        return new RestResponse();
    }
}
