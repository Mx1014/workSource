package com.everhomes.archives;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.archives.*;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.GetImportFileResultCommand;
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

    @Autowired
    private ArchivesFormService archivesFormService;

    @Autowired
    private ArchivesDTSService archivesDTSService;

    /**
     * <b>URL: /archives/addArchivesContact</b>
     * <p>1.添加与编辑通讯录成员</p>
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
     * <p>2-1.调动通讯录成员</p>
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
     * <p>3.置顶通讯录成员</p>
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
    public RestResponse
    listArchivesContacts(ListArchivesContactsCommand cmd){
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
        RestResponse response = new RestResponse(archivesDTSService.importArchivesContacts(files[0], user.getId(),user.getNamespaceId(),cmd));
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
    public RestResponse exportArchivesContacts(ListArchivesContactsCommand cmd){
        archivesDTSService.exportArchivesContacts(cmd);
        return new RestResponse();
    }

    /**
     * <b>URL: /archives/verifyPersonnelByPassword</b>
     * <p>5-3.导出身份校验</p>
     */
    @RequestMapping("verifyPersonnelByPassword")
    @RestReturn(value = String.class)
    public RestResponse verifyPersonnelByPassword(VerifyPersonnelByPasswordCommand cmd){
        archivesDTSService.verifyPersonnelByPassword(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/getImportContactsResult</b>
     * <p>5-4.获取通讯录导入结果</p>
     */
    @RequestMapping("getImportContactsResult")
    @RestReturn(value = ImportFileResponse.class)
    public RestResponse getImportContactsResult(GetImportFileResultCommand cmd) {
        RestResponse response = new RestResponse(archivesDTSService.getImportContactsResult(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/exportImportFileFailResults</b>
     * <p>5-5.导出错误结果</p>
     */
    @RequestMapping("exportImportFileFailResults")
    @RestReturn(value = String.class)
    public RestResponse exportImportFileFailResults(GetImportFileResultCommand cmd, HttpServletResponse httpResponse) {
        archivesDTSService.exportImportFileFailResults(cmd, httpResponse);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/addArchivesEmployee</b>
     * <p>6-1.添加成员至员工档案</p>
     */
    @RequestMapping("addArchivesEmployee")
    @RestReturn(value = ArchivesEmployeeDTO.class)
    public RestResponse addArchivesEmployee(AddArchivesEmployeeCommand cmd){
        ArchivesEmployeeDTO res = archivesService.addArchivesEmployee(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/updateArchivesEmployee</b>
     * <p>6-2.编辑员工档案</p>
     */
    @RequestMapping("updateArchivesEmployee")
    @RestReturn(value = String.class)
    public RestResponse updateArchivesEmployee(UpdateArchivesEmployeeCommand cmd){
        archivesService.updateArchivesEmployee(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/updateArchivesEmployeeAvatar</b>
     * <p>6-3.更新员工头像</p>
     */
    @RequestMapping("updateArchivesEmployeeAvatar")
    @RestReturn(value = String.class)
    public RestResponse updateArchivesEmployeeAvatar(UpdateArchivesEmployeeCommand cmd){
        archivesService.updateArchivesEmployeeAvatar(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/getArchivesEmployee</b>
     * <p>6-4.查看员工档案详情</p>
     */
    @RequestMapping("getArchivesEmployee")
    @RestReturn(value = GetArchivesEmployeeResponse.class)
    public RestResponse getArchivesEmployee(GetArchivesEmployeeCommand cmd){
        GetArchivesEmployeeResponse res = archivesService.getArchivesEmployee(cmd);
        RestResponse response = new RestResponse(res);
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
     * <b>URL: /archives/employArchivesEmployees</b>
     * <p>8-1.转正员工转正</p>
     */
    @RequestMapping("employArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse employArchivesEmployees(EmployArchivesEmployeesCommand cmd){
        archivesService.employArchivesEmployeesConfig(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/transferArchivesEmployees</b>
     * <p>8-2.发起员工调动</p>
     */
    @RequestMapping("transferArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse transferArchivesEmployees(TransferArchivesEmployeesCommand cmd){
        archivesService.transferArchivesEmployeesConfig(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/dismissArchivesEmployees</b>
     * <p>8-3.发起员工离职</p>
     */
    @RequestMapping("dismissArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse dismissArchivesEmployees(DismissArchivesEmployeesCommand cmd){
        archivesService.dismissArchivesEmployeesConfig(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/deleteArchivesEmployees</b>
     * <p>8-4.删除员工</p>
     */
    @RequestMapping("deleteArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse deleteArchivesEmployees(DeleteArchivesEmployeesCommand cmd){
        archivesService.deleteArchivesEmployees(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/checkArchivesOperation</b>
     * <p>8-5.校验已存在的定时操作</p>
     */
    @RequestMapping("checkArchivesOperation")
    @RestReturn(value = CheckOperationResponse.class)
    public RestResponse checkArchivesOperation(CheckOperationCommand cmd){
        CheckOperationResponse res = archivesService.checkArchivesOperation(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/listArchivesDismissCategories</b>
     * <p>9-1.获取离职原因</p>
     */
    @RequestMapping("listArchivesDismissCategories")
    @RestReturn(value = ListDismissCategoriesResponse.class)
    public RestResponse listArchivesDismissReason(){
        ListDismissCategoriesResponse res = archivesService.listArchivesDismissCategories();
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/listArchivesDismissEmployees</b>
     * <p>9-2.离职员工列表</p>
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
     * <b>URL: /archives/updateArchivesForm</b>
     * <p>10-1.增加、修改档案字段</p>
     */
    @RequestMapping("updateArchivesForm")
    @RestReturn(value = String.class)
    public RestResponse updateArchivesForm(UpdateArchivesFormCommand cmd){
        archivesFormService.updateArchivesForm(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/getArchivesForm</b>
     * <p>10-2.获取档案字段</p>
     */
    @RequestMapping("getArchivesForm")
    @RestReturn(value = GetArchivesFormResponse.class)
    public RestResponse getArchivesForm(GetArchivesFormCommand cmd){
        GetArchivesFormResponse res = archivesFormService.getArchivesForm(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

/*    *//**
     * <b>URL: /archives/identifyArchivesForm</b>
     * <p>10-3.识别档案表单id</p>
     *//*
    @RequestMapping("identifyArchivesForm")
    @RestReturn(value = ArchivesFromsDTO.class)
    public RestResponse identifyArchivesForm(IdentifyArchivesFormCommand cmd){
        ArchivesFromsDTO res = archivesService.identifyArchivesForm(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }*/

    /**
     * <b>URL: /archives/importArchivesEmployees</b>
     * <p>11-1.导入人事档案成员</p>
     */
    @RequestMapping("importArchivesEmployees")
    @RestReturn(value = ImportFileTaskDTO.class)
    public RestResponse importArchivesEmployees(ImportArchivesEmployeesCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
        RestResponse response = new RestResponse(archivesDTSService.importArchivesEmployees(files[0],cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/exportArchivesEmployees</b>
     * <p>11-2.导出人事档案成员</p>
     */
    @RequestMapping("exportArchivesEmployees")
    @RestReturn(value = String.class)
    public RestResponse exportArchivesEmployees(ExportArchivesEmployeesCommand cmd){
        archivesDTSService.exportArchivesEmployees(cmd);
        return new RestResponse();
    }

    /**
     * <b>URL: /archives/exportArchivesEmployeesTemplate</b>
     * <p>11-3.导出人事档案导入模板</p>
     */
    @RequestMapping("exportArchivesEmployeesTemplate")
    @RestReturn(value = String.class)
    public RestResponse exportArchivesEmployeesTemplate(ExportArchivesEmployeesTemplateCommand cmd, HttpServletResponse httpResponse){
        archivesDTSService.exportArchivesEmployeesTemplate(cmd,httpResponse);
        return new RestResponse();
    }

    /**
     * <b>URL: /archives/getImportEmployeesResult</b>
     * <p>11-4.获取人事档案导入结果</p>
     */
    @RequestMapping("getImportEmployeesResult")
    @RestReturn(value = ImportFileResponse.class)
    public RestResponse getImportEmployeesResult(GetImportFileResultCommand cmd) {
        RestResponse response = new RestResponse(archivesDTSService.getImportEmployeesResult(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/setArchivesNotification</b>
     * <p>12-1.人事档案提醒设置</p>
     */
    @RequestMapping("setArchivesNotification")
    @RestReturn(value = String.class)
    public RestResponse setArchivesNotification(ArchivesNotificationCommand cmd){
        archivesService.setArchivesNotification(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/getArchivesNotification</b>
     * <p>12-2.获取原提醒设置</p>
     */
    @RequestMapping("getArchivesNotification")
    @RestReturn(value = ArchivesNotificationDTO.class)
    public RestResponse getArchivesNotification(ArchivesNotificationCommand cmd){
        ArchivesNotificationDTO res = archivesService.getArchivesNotification(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/executeArchivesConfiguration</b>
     * <p>(转正,调整,离职 配置项手动调用定时器)</p>
     */
    @RequestMapping("executeArchivesConfiguration")
    @RestReturn(value = String.class)
    public RestResponse executeArchivesConfiguration(){
        archivesService.executeArchivesConfiguration();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/makeArchivesCheckInTime</b>
     * <p>刷入职时间 (当版本高于5.8.0后可以删除该接口)</p>
     */
    @RequestMapping("makeArchivesCheckInTime")
    @RestReturn(value = String.class)
    public RestResponse makeArchivesCheckInTime(){
        archivesService.makeArchivesCheckInTime();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /archives/cleanRedundantArchivesDetails</b>
     * <p>清除人事档案表中的辣鸡数据 (当版本高于5.8.0后可以删除该接口)</p>
     */
    @RequestMapping("cleanRedundantArchivesDetails")
    @RestReturn(value = String.class)
    public RestResponse cleanRedundantArchivesDetails(){
        archivesService.cleanRedundantArchivesDetails();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
