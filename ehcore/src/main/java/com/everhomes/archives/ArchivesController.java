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
@RequestMapping("/archives")
public class ArchivesController extends ControllerBase{

    @Autowired
    private ArchivesService archivesService;

    /**
     * <b>URL: /archives/addArchivesContact</b>
     * <p>1.添加、编辑通讯录成员</p>
     */
    @RequestMapping("addArchivesContact")
    @RestReturn(value = ArchivesContactDTO.class)
    public RestResponse addArchivesContact(AddArchivesContactCommand cmd){
        ArchivesContactDTO res = archivesService.addArchivesContact(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/transferArchivesContacts</b>
     * <p>2-1.调整通讯录成员</p>
     */
    @RequestMapping("transferArchivesContacts")
    @RestReturn(value = String.class)
    public RestResponse transferArchivesContacts(TransferArchivesContactsCommand cmd){
        archivesService.transferArchivesContacts(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/deleteArchivesContacts</b>
     * <p>2-2.删除通讯录成员</p>
     */
    @RequestMapping("deleteArchivesContacts")
    @RestReturn(value = String.class)
    public RestResponse deleteArchivesContacts(DeleteArchivesContactsCommand cmd){
        archivesService.deleteArchivesContacts(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/stickArchivesContact</b>
     * <p>3.通讯录成员置顶</p>
     */
    @RequestMapping("stickArchivesContact")
    @RestReturn(value = String.class)
    public RestResponse stickArchivesContact(StickArchivesContactCommand cmd){
        archivesService.stickArchivesContact(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/listArchivesContacts</b>
     * <p>4.通讯录成员列表</p>
     */
    @RequestMapping("listArchivesContacts")
    @RestReturn(value = ListArchivesContactsResponse.class)
    public RestResponse listArchivesContacts(ListArchivesContactsCommand cmd){
        ListArchivesContactsResponse res = archivesService.listArchivesContacts(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/importArchivesContacts</b>
     * <p>5-1.导入通讯录成员</p>
     */
    @RequestMapping("importArchivesContacts")
    @RestReturn(value = String.class)
    public RestResponse importArchivesContacts(ImportArchivesContactsCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
        User user = UserContext.current().getUser();
        RestResponse response = new RestResponse(archivesService.importArchivesContacts(files[0], user.getId(),user.getNamespaceId(),cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/exportArchivesContacts</b>
     * <p>5-2.导出通讯录成员</p>
     */
    @RequestMapping("exportArchivesContacts")
    @RestReturn(value = String.class)
    public RestResponse exportArchivesContacts(ExportArchivesContactsCommand cmd, HttpServletResponse httpResponse){
        archivesService.exportArchivesContacts(cmd,httpResponse);
        return new RestResponse();
    }

    /**
     * <b>URL: /archives/verifyPersonnelByPassword</b>
     * <p>5-3.导出通讯录身份校验</p>
     */
    @RequestMapping("verifyPersonnelByPassword")
    @RestReturn(value = String.class)
    public RestResponse verifyPersonnelByPassword(VerifyPersonnelByPasswordCommand cmd){
        archivesService.verifyPersonnelByPassword(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/listArchivesEmployees</b>
     * <p>7.员工列表</p>
     */
    @RequestMapping("listArchivesEmployees")
    @RestReturn(value = ListArchivesEmployeesResponse.class)
    public RestResponse listArchivesEmployees(ListArchivesEmployeesCommand cmd){
        ListArchivesEmployeesResponse res = archivesService.listArchivesEmployees(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/addArchivesEmployee</b>
     * <p>8.添加成员至员工档案</p>
     */
    @RequestMapping("addArchivesEmployee")
    @RestReturn(value = ArchivesEmployeeDTO.class)
    public RestResponse addArchivesEmployee(AddArchivesEmployeeCommand cmd){
        ArchivesEmployeeDTO res = archivesService.addArchivesEmployee(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/listArchivesDismissEmployees</b>
     * <p>9.离职员工列表</p>
     */
    @RequestMapping("listArchivesDismissEmployees")
    @RestReturn(value = ListArchivesEmployeesResponse.class)
    public RestResponse listArchivesDismissEmployees(ListArchivesDismissEmployeesCommand cmd){
        ListArchivesDismissEmployeesResponse res = archivesService.listArchivesDismissEmployees(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/employArchivesEmployees</b>
     * <p>10-1.员工批量转正</p>
     */
    @RequestMapping("employArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse employArchivesEmployees(EmployArchivesEmployeesCommand cmd){
        archivesService.employArchivesEmployees(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/transferArchivesEmployees</b>
     * <p>10-2.员工批量调整</p>
     */
    @RequestMapping("transferArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse transferArchivesEmployees(TransferArchivesEmployeesCommand cmd){
        archivesService.transferArchivesEmployees(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/dismissArchivesEmployees</b>
     * <p>10-3.员工批量离职</p>
     */
    @RequestMapping("dismissArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse dismissArchivesEmployees(DismissArchivesEmployeesCommand cmd){
        archivesService.dismissArchivesEmployees(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/addArchivesField</b>
     * <p>11-1.增加、修改档案字段</p>
     */
    @RequestMapping("addArchivesField")
    @RestReturn(value = String.class)
    public RestResponse addArchivesField(AddArchivesFieldCommand cmd){
        archivesService.addArchivesField(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/addArchivesFieldGroup</b>
     * <p>11-2.添加档案字段分组</p>
     */
    @RequestMapping("addArchivesFieldGroup")
    @RestReturn(value = String.class)
    public RestResponse addArchivesFieldGroup(AddArchivesFieldGroupCommand cmd){
        archivesService.addArchivesFieldGroup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/updateArchivesFieldOrder</b>
     * <p>11-3.批量调整档案字段</p>
     */
    @RequestMapping("updateArchivesFieldOrder")
    @RestReturn(value = String.class)
    public RestResponse updateArchivesFieldOrder(UpdateArchivesFieldOrderCommand cmd){
        archivesService.updateArchivesFieldOrder(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/getArchivesField</b>
     * <p>11-4.获取档案字段</p>
     */
    @RequestMapping("getArchivesField")
    @RestReturn(value = String.class)
    public RestResponse getArchivesField(GetArchivesFieldCommand cmd){
        GetArchivesFieldResponse res = archivesService.getArchivesField(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
