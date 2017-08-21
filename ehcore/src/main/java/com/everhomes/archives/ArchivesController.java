package com.everhomes.archives;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.profile.ProfileService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.archives.*;
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
     * <b>URL: /profile/addProfileContact</b>
     * <p>1.添加、编辑通讯录成员</p>
     */
    @RequestMapping("addProfileContact")
    @RestReturn(value = ArchivesContactDTO.class)
    public RestResponse addProfileContact(AddArchivesContactCommand cmd){
        ArchivesContactDTO res = profileService.addProfileContact(cmd);
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
    public RestResponse transferProfileContacts(TransferArchivesContactsCommand cmd){
        profileService.transferProfileContacts(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/deleteProfileContacts</b>
     * <p>2-2.删除通讯录成员</p>
     */
    @RequestMapping("deleteProfileContacts")
    @RestReturn(value = String.class)
    public RestResponse deleteProfileContacts(DeleteArchivesContactsCommand cmd){
        profileService.deleteProfileContacts(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/stickProfileContact</b>
     * <p>3.通讯录成员置顶</p>
     */
    @RequestMapping("stickProfileContact")
    @RestReturn(value = String.class)
    public RestResponse stickProfileContact(StickArchivesContactCommand cmd){
        profileService.stickProfileContact(cmd);
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
    @RestReturn(value = ListArchivesContactsResponse.class)
    public RestResponse listProfileContacts(ListArchivesContactsCommand cmd){
        ListArchivesContactsResponse res = profileService.listProfileContacts(cmd);
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
    public RestResponse importProfileContacts(ImportArchivesContactsCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
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
    public RestResponse exportProfileContacts(ExportArchivesContactsCommand cmd, HttpServletResponse httpResponse){
        profileService.exportProfileContacts(cmd,httpResponse);
        return new RestResponse();
    }

    /**
     * <b>URL: /profile/verifyPersonnelByPassword</b>
     * <p>5-3.导出通讯录身份校验</p>
     */
    @RequestMapping("verifyPersonnelByPassword")
    @RestReturn(value = String.class)
    public RestResponse verifyPersonnelByPassword(VerifyPersonnelByPasswordCommand cmd){
        profileService.verifyPersonnelByPassword(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/listProfileEmployees</b>
     * <p>7.员工列表</p>
     */
    @RequestMapping("listProfileEmployees")
    @RestReturn(value = ListArchivesEmployeesResponse.class)
    public RestResponse listProfileEmployees(ListArchivesEmployeesCommand cmd){
        ListArchivesEmployeesResponse res = profileService.listProfileEmployees(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/addProfileEmployee</b>
     * <p>8.添加成员至员工档案</p>
     */
    @RequestMapping("addProfileEmployee")
    @RestReturn(value = ArchivesEmployeeDTO.class)
    public RestResponse addProfileEmployee(AddArchivesEmployeeCommand cmd){
        ArchivesEmployeeDTO res = profileService.addProfileEmployee(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/listProfileDismissEmployees</b>
     * <p>9.离职员工列表</p>
     */
    @RequestMapping("listProfileDismissEmployees")
    @RestReturn(value = ListArchivesEmployeesResponse.class)
    public RestResponse listProfileDismissEmployees(ListArchivesDismissEmployeesCommand cmd){
        ListArchivesDismissEmployeesResponse res = profileService.listProfileDismissEmployees(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/employProfileEmployees</b>
     * <p>10-1.员工批量转正</p>
     */
    @RequestMapping("employProfileEmployees")
    @RestReturn(value = String.class)
    public RestResponse employProfileEmployees(EmployArchivesEmployeesCommand cmd){
        profileService.employProfileEmployees(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/transferProfileEmployees</b>
     * <p>10-2.员工批量调整</p>
     */
    @RequestMapping("transferProfileEmployees")
    @RestReturn(value = String.class)
    public RestResponse transferProfileEmployees(TransferArchivesEmployeesCommand cmd){
        profileService.transferProfileEmployees(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/dismissProfileEmployees</b>
     * <p>10-3.员工批量离职</p>
     */
    @RequestMapping("dismissProfileEmployees")
    @RestReturn(value = String.class)
    public RestResponse dismissProfileEmployees(DismissArchivesEmployeesCommand cmd){
        profileService.dismissProfileEmployees(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/addProfileField</b>
     * <p>11-1.增加、修改档案字段</p>
     */
    @RequestMapping("addProfileField")
    @RestReturn(value = String.class)
    public RestResponse addProfileField(AddArchivesFieldCommand cmd){
        profileService.addProfileField(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/addProfileFieldGroup</b>
     * <p>11-2.添加档案字段分组</p>
     */
    @RequestMapping("addProfileFieldGroup")
    @RestReturn(value = String.class)
    public RestResponse addProfileFieldGroup(AddArchivesFieldGroupCommand cmd){
        profileService.addProfileFieldGroup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/updateProfileFieldOrder</b>
     * <p>11-3.批量调整档案字段</p>
     */
    @RequestMapping("updateProfileFieldOrder")
    @RestReturn(value = String.class)
    public RestResponse updateProfileFieldOrder(UpdateArchivesFieldOrderCommand cmd){
        profileService.updateProfileFieldOrder(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/getProfileField</b>
     * <p>11-4.获取档案字段</p>
     */
    @RequestMapping("getProfileField")
    @RestReturn(value = String.class)
    public RestResponse getProfileField(GetArchivesFieldCommand cmd){
        GetArchivesFieldResponse res = profileService.getProfileField(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
