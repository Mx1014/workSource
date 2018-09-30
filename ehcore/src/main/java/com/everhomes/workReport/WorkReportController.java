package com.everhomes.workReport;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.workReport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 2017.11.30
 * 工作汇报
 */

@RestController
@RequestMapping("/workReport")
public class WorkReportController extends ControllerBase{

    @Autowired
    private WorkReportService workReportService;

    @Autowired
    private WorkReportMessageService workReportMessageService;

    /**
     * <b>URL: /workReport/addWorkReport</b>
     * <p>1-1.新增汇报</p>
     */
    @RequestMapping("addWorkReport")
    @RestReturn(value = WorkReportDTO.class)
    public RestResponse addWorkReport(AddWorkReportCommand cmd){
        WorkReportDTO res = workReportService.addWorkReport(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/deleteWorkReport</b>
     * <p>1-2.删除汇报</p>
     */
    @RequestMapping("deleteWorkReport")
    @RestReturn(value = String.class)
    public RestResponse deleteWorkReport(WorkReportIdCommand cmd){
        workReportService.deleteWorkReport(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/updateWorkReport</b>
     * <p>1-3.汇报规则设置</p>
     */
    @RequestMapping("updateWorkReport")
    @RestReturn(value = WorkReportDTO.class)
    public RestResponse updateWorkReport(UpdateWorkReportCommand cmd){
        WorkReportDTO res = workReportService.updateWorkReport(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/getWorkReport</b>
     * <p>1-4.获取汇报详情</p>
     */
    @RequestMapping("getWorkReport")
    @RestReturn(value = WorkReportDTO.class)
    public RestResponse getWorkReport(WorkReportIdCommand cmd){
        WorkReportDTO res = workReportService.getWorkReport(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/listWorkReports</b>
     * <p>1-5.工作汇报列表</p>
     */
    @RequestMapping("listWorkReports")
    @RestReturn(value = ListWorkReportsResponse.class)
    public RestResponse listWorkReports(ListWorkReportsCommand cmd){
        ListWorkReportsResponse res = workReportService.listWorkReports(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/updateWorkReportName</b>
     * <p>1-6.修改汇报名称</p>
     */
    @RequestMapping("updateWorkReportName")
    @RestReturn(value = WorkReportDTO.class)
    public RestResponse updateWorkReportName(UpdateWorkReportNameCommand cmd){
        WorkReportDTO res = workReportService.updateWorkReportName(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/enableWorkReport</b>
     * <p>1-7.启用工作汇报</p>
     * <p>(若 formOriginId 为 0 时则不能执行此方法，具体流程类似于审批设置的启用)</p>
     */
    @RequestMapping("enableWorkReport")
    @RestReturn(value = String.class)
    public RestResponse enableWorkReport(WorkReportIdCommand cmd){
        workReportService.enableWorkReport(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/disableWorkReport</b>
     * <p>1-8.关闭工作汇报</p>
     */
    @RequestMapping("disableWorkReport")
    @RestReturn(value = String.class)
    public RestResponse disableWorkReport(WorkReportIdCommand cmd){
        workReportService.disableWorkReport(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/verifyWorkReportTemplates</b>
     * <p>2-1.判断是否需要创建工作汇报模板 </p>
     */
    @RequestMapping("verifyWorkReportTemplates")
    @RestReturn(value=VerifyWorkReportResponse.class)
    public RestResponse verifyWorkReportTemplates(CreateWorkReportTemplatesCommand cmd) {
        VerifyWorkReportResponse res = workReportService.verifyWorkReportTemplates(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/createWorkReportTemplates</b>
     * <p>2-2.创建工作汇报模板 </p>
     */
    @RequestMapping("createWorkReportTemplates")
    @RestReturn(value=String.class)
    public RestResponse createWorkReportTemplates(CreateWorkReportTemplatesCommand cmd) {
        workReportService.createWorkReportTemplates(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/listActiveWorkReports</b>
     * <p>3.指定范围的工作汇报列表</p>
     */
    @RequestMapping("listActiveWorkReports")
    @RestReturn(value = ListWorkReportsResponse.class)
    public RestResponse listActiveWorkReports(ListWorkReportsCommand cmd){
        ListWorkReportsResponse res = workReportService.listActiveWorkReports(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/postWorkReportVal</b>
     * <p>4-1.提交工作汇报单 </p>
     */
    @RequestMapping("postWorkReportVal")
    @RestReturn(value=WorkReportValDTO.class)
    public RestResponse postWorkReportVal(PostWorkReportValCommand cmd) {
        WorkReportValDTO res = workReportService.postWorkReportVal(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/deleteWorkReportVal</b>
     * <p>4-2.删除工作汇报单 </p>
     */
    @RequestMapping("deleteWorkReportVal")
    @RestReturn(value=String.class)
    public RestResponse deleteWorkReportVal(WorkReportValIdCommand cmd) {
        workReportService.deleteWorkReportVal(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/updateWorkReportVal</b>
     * <p>4-3.编辑工作汇报单 </p>
     */
    @RequestMapping("updateWorkReportVal")
    @RestReturn(value=WorkReportValDTO.class)
    public RestResponse updateWorkReportVal(PostWorkReportValCommand cmd) {
        WorkReportValDTO res = workReportService.updateWorkReportVal(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/getWorkReportValItem</b>
     * <p>4-4.获取工作汇报单填写项 </p>
     * <p>(此方法在写汇报与编辑汇报之前调用)</p>
     */
    @RequestMapping("getWorkReportValItem")
    @RestReturn(value=WorkReportValDTO.class)
    public RestResponse getWorkReportValItem(WorkReportValIdCommand cmd) {
        WorkReportValDTO res = workReportService.getWorkReportValItem(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /workReport/listSubmittedWorkReportsVal</b>
     * <p>4-5.“我提交的”工作汇报单列表 </p>
     */
    @RequestMapping("listSubmittedWorkReportsVal")
    @RestReturn(value=ListWorkReportsValResponse.class)
    public RestResponse listSubmittedWorkReportsVal(ListWorkReportsValCommand cmd) {
        ListWorkReportsValResponse res = workReportService.listSubmittedWorkReportsVal(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/listReceivedWorkReportsVal</b>
     * <p>4-6.“我接收的”工作汇报单列表 </p>
     */
    @RequestMapping("listReceivedWorkReportsVal")
    @RestReturn(value=ListWorkReportsValResponse.class)
    public RestResponse listReceivedWorkReportsVal(ListWorkReportsValCommand cmd) {
        ListWorkReportsValResponse res = workReportService.listReceivedWorkReportsVal(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/countUnReadWorkReportsVal</b>
     * <p>4-7.获取汇报单未读条目数 </p>
     */
    @RequestMapping("countUnReadWorkReportsVal")
    @RestReturn(value=Integer.class)
    public RestResponse countUnReadWorkReportsVal(WorkReportOrgIdCommand cmd) {
        Integer res = workReportService.countUnReadWorkReportsVal(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/markWorkReportsValReading</b>
     * <p>4-8.当前用户汇报单均标记为已读 </p>
     */
    @RequestMapping("markWorkReportsValReading")
    @RestReturn(value=String.class)
    public RestResponse markWorkReportsValReading(WorkReportOrgIdCommand cmd) {
        workReportService.markWorkReportsValReading(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/getWorkReportValDetail</b>
     * <p>5.获取工作汇报单详情 </p>
     */
    @RequestMapping("getWorkReportValDetail")
    @RestReturn(value=WorkReportValDTO.class)
    public RestResponse getWorkReportValDetail(WorkReportValIdCommand cmd) {
        WorkReportValDTO res = workReportService.getWorkReportValDetail(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/syncWorkReportReceiver</b>
     * <p>同步工作汇报接收人公司信息(当版本高于5.9.0后可以删除该接口) </p>
     */
    @RequestMapping("syncWorkReportReceiver")
    @RestReturn(value=String.class)
    public RestResponse syncWorkReportReceiver() {
        workReportService.syncWorkReportReceiver();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/updateWorkReportReceiverAvatar</b>
     * <p>更新工作汇报接收人头像(当版本高于5.9.0后可以删除该接口) </p>
     */
    @RequestMapping("updateWorkReportReceiverAvatar")
    @RestReturn(value=String.class)
    public RestResponse updateWorkReportReceiverAvatar() {
        workReportService.updateWorkReportReceiverAvatar();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/updateWorkReportValAvatar</b>
     * <p>更新工作汇值头像 </p>
     */
    @RequestMapping("updateWorkReportValAvatar")
    @RestReturn(value=String.class)
    public RestResponse updateWorkReportValAvatar() {
        workReportService.updateWorkReportValAvatar();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
