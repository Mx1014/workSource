package com.everhomes.archives;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.archives.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
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
     * <p>2.调整通讯录成员</p>
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
    @RestReturn(value = ImportFileTaskDTO.class)
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
     * <b>URL: /archives/deleteArchivesContacts</b>
     * <p>6.批量删除通讯录、员工</p>
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
     * <b>URL: /archives/listArchivesEmployees</b>
     * <p>8.员工列表</p>
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
     * <p>9.添加成员至员工档案</p>
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
     * <b>URL: /archives/getArchivesEmployee</b>
     * <p>10.员工档案详情</p>
     */
    @RequestMapping("getArchivesEmployee")
    @RestReturn(value = GetArchivesEmployeeResponse.class)
    public RestResponse getArchivesEmployee(GetArchivesEmployeeCommand cmd){
        GetArchivesEmployeeResponse res = archivesService.getArchivesEmployee(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/listArchivesDismissEmployees</b>
     * <p>11.离职员工列表</p>
     */
    @RequestMapping("listArchivesDismissEmployees")
    @RestReturn(value = ListArchivesDismissEmployeesResponse.class)
    public RestResponse listArchivesDismissEmployees(ListArchivesDismissEmployeesCommand cmd){
        ListArchivesDismissEmployeesResponse res = archivesService.listArchivesDismissEmployees(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/employArchivesEmployees</b>
     * <p>12-1.员工批量转正</p>
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
     * <p>12-2.员工批量调整</p>
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
     * <p>12-3.员工批量离职</p>
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
     * <b>URL: /archives/updateArchivesForm</b>
     * <p>13-1.增加、修改档案字段</p>
     */
    @RequestMapping("updateArchivesForm")
    @RestReturn(value = String.class)
    public RestResponse updateArchivesForm(UpdateArchivesFormCommand cmd){
        archivesService.updateArchivesForm(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/getArchivesForm</b>
     * <p>13-2.获取档案字段</p>
     */
    @RequestMapping("getArchivesForm")
    @RestReturn(value = GetArchivesFormResponse.class)
    public RestResponse getArchivesForm(GetArchivesFormCommand cmd){
        GetArchivesFormResponse res = archivesService.getArchivesForm(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/identifyArchivesForm</b>
     * <p>13-3.识别档案表单id</p>
     */
    @RequestMapping("identifyArchivesForm")
    @RestReturn(value = Long.class)
    public RestResponse identifyArchivesForm(IdentifyArchivesFormCommand cmd){
        Long res = archivesService.identifyArchivesForm(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/importArchivesEmployees</b>
     * <p>14-1.导入人事档案成员</p>
     */
    @RequestMapping("importArchivesEmployees")
    @RestReturn(value = ImportFileTaskDTO.class)
    public RestResponse importArchivesEmployees(ImportArchivesEmployeesCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
        User user = UserContext.current().getUser();
        RestResponse response = new RestResponse(archivesService.importArchivesEmployees(files[0], user.getId(),user.getNamespaceId(),cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/exportArchivesEmployees</b>
     * <p>14-2.导出人事档案成员</p>
     */
    @RequestMapping("exportArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse exportArchivesEmployees(ExportArchivesEmployeesCommand cmd, HttpServletResponse httpResponse){
        archivesService.exportArchivesEmployees(cmd,httpResponse);
        return new RestResponse();
    }

    /**
     * <b>URL: /archives/remindArchivesEmployee</b>
     * <p>15.为员工做提醒设置</p>
     */
    @RequestMapping("remindArchivesEmployee")
    @RestReturn(value = String.class)
    public RestResponse remindArchivesEmployee(RemindArchivesEmployeeCommand cmd){
        archivesService.remindArchivesEmployee(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
