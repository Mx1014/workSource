package com.everhomes.archives;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
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
public class ArchivesController extends ControllerBase{

    @Autowired
    private ArchivesService profileService;

    /**
     * <b>URL: /profile/addArchivesContact</b>
     * <p>1.添加、编辑通讯录成员</p>
     */
    @RequestMapping("addArchivesContact")
    @RestReturn(value = ArchivesContactDTO.class)
    public RestResponse addArchivesContact(AddArchivesContactCommand cmd){
        ArchivesContactDTO res = profileService.addArchivesContact(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/transferArchivesContacts</b>
     * <p>2-1.调整通讯录成员</p>
     */
    @RequestMapping("transferArchivesContacts")
    @RestReturn(value = String.class)
    public RestResponse transferArchivesContacts(TransferArchivesContactsCommand cmd){
        profileService.transferArchivesContacts(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/deleteArchivesContacts</b>
     * <p>2-2.删除通讯录成员</p>
     */
    @RequestMapping("deleteArchivesContacts")
    @RestReturn(value = String.class)
    public RestResponse deleteArchivesContacts(DeleteArchivesContactsCommand cmd){
        profileService.deleteArchivesContacts(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/stickArchivesContact</b>
     * <p>3.通讯录成员置顶</p>
     */
    @RequestMapping("stickArchivesContact")
    @RestReturn(value = String.class)
    public RestResponse stickArchivesContact(StickArchivesContactCommand cmd){
        profileService.stickArchivesContact(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/listArchivesContacts</b>
     * <p>4.通讯录成员列表</p>
     */
    @RequestMapping("listArchivesContacts")
    @RestReturn(value = ListArchivesContactsResponse.class)
    public RestResponse listArchivesContacts(ListArchivesContactsCommand cmd){
        ListArchivesContactsResponse res = profileService.listArchivesContacts(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/importArchivesContacts</b>
     * <p>5-1.导入通讯录成员</p>
     */
    @RequestMapping("importArchivesContacts")
    @RestReturn(value = String.class)
    public RestResponse importArchivesContacts(ImportArchivesContactsCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
        User user = UserContext.current().getUser();
        RestResponse response = new RestResponse(profileService.importArchivesContacts(files[0], user.getId(),user.getNamespaceId(),cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/exportArchivesContacts</b>
     * <p>5-2.导出通讯录成员</p>
     */
    @RequestMapping("exportArchivesContacts")
    @RestReturn(value = String.class)
    public RestResponse exportArchivesContacts(ExportArchivesContactsCommand cmd, HttpServletResponse httpResponse){
        profileService.exportArchivesContacts(cmd,httpResponse);
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
     * <b>URL: /profile/listArchivesEmployees</b>
     * <p>7.员工列表</p>
     */
    @RequestMapping("listArchivesEmployees")
    @RestReturn(value = ListArchivesEmployeesResponse.class)
    public RestResponse listArchivesEmployees(ListArchivesEmployeesCommand cmd){
        ListArchivesEmployeesResponse res = profileService.listArchivesEmployees(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/addArchivesEmployee</b>
     * <p>8.添加成员至员工档案</p>
     */
    @RequestMapping("addArchivesEmployee")
    @RestReturn(value = ArchivesEmployeeDTO.class)
    public RestResponse addArchivesEmployee(AddArchivesEmployeeCommand cmd){
        ArchivesEmployeeDTO res = profileService.addArchivesEmployee(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/listArchivesDismissEmployees</b>
     * <p>9.离职员工列表</p>
     */
    @RequestMapping("listArchivesDismissEmployees")
    @RestReturn(value = ListArchivesEmployeesResponse.class)
    public RestResponse listArchivesDismissEmployees(ListArchivesDismissEmployeesCommand cmd){
        ListArchivesDismissEmployeesResponse res = profileService.listArchivesDismissEmployees(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/employArchivesEmployees</b>
     * <p>10-1.员工批量转正</p>
     */
    @RequestMapping("employArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse employArchivesEmployees(EmployArchivesEmployeesCommand cmd){
        profileService.employArchivesEmployees(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/transferArchivesEmployees</b>
     * <p>10-2.员工批量调整</p>
     */
    @RequestMapping("transferArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse transferArchivesEmployees(TransferArchivesEmployeesCommand cmd){
        profileService.transferArchivesEmployees(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/dismissArchivesEmployees</b>
     * <p>10-3.员工批量离职</p>
     */
    @RequestMapping("dismissArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse dismissArchivesEmployees(DismissArchivesEmployeesCommand cmd){
        profileService.dismissArchivesEmployees(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/addArchivesField</b>
     * <p>11-1.增加、修改档案字段</p>
     */
    @RequestMapping("addArchivesField")
    @RestReturn(value = String.class)
    public RestResponse addArchivesField(AddArchivesFieldCommand cmd){
        profileService.addArchivesField(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/addArchivesFieldGroup</b>
     * <p>11-2.添加档案字段分组</p>
     */
    @RequestMapping("addArchivesFieldGroup")
    @RestReturn(value = String.class)
    public RestResponse addArchivesFieldGroup(AddArchivesFieldGroupCommand cmd){
        profileService.addArchivesFieldGroup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/updateArchivesFieldOrder</b>
     * <p>11-3.批量调整档案字段</p>
     */
    @RequestMapping("updateArchivesFieldOrder")
    @RestReturn(value = String.class)
    public RestResponse updateArchivesFieldOrder(UpdateArchivesFieldOrderCommand cmd){
        profileService.updateArchivesFieldOrder(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /profile/getArchivesField</b>
     * <p>11-4.获取档案字段</p>
     */
    @RequestMapping("getArchivesField")
    @RestReturn(value = String.class)
    public RestResponse getArchivesField(GetArchivesFieldCommand cmd){
        GetArchivesFieldResponse res = profileService.getArchivesField(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
