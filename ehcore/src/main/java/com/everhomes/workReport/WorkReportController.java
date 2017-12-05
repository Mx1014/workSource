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
     * <b>URL: /workReport/listWorkReports</b>
     * <p>1-4.工作汇报列表</p>
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
     * <p>1-5.修改汇报名称</p>
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
     * <p>1-6.启用工作汇报</p>
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
     * <p>1-7.关闭工作汇报</p>
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
     * <b>URL: /workReport/postWorkReportVal</b>
     * <p>3-1.提交、编辑工作汇报 </p>
     */
    @RequestMapping("postWorkReportVal")
    @RestReturn(value=String.class)
    public RestResponse postWorkReportVal(PostWorkReportValCommand cmd) {
        workReportService.postWorkReportVal(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/updateWorkReportVal</b>
     * <p>3-2.编辑工作汇报 </p>
     */
    @RequestMapping("updateWorkReportVal")
    @RestReturn(value=String.class)
    public RestResponse updateWorkReportVal(PostWorkReportValCommand cmd) {
        workReportService.updateWorkReportVal(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/listWorkReportsVal</b>
     * <p>3-3.工作汇报申请列表 </p>
     */
    @RequestMapping("listWorkReportsVal")
    @RestReturn(value=ListWorkReportsValResponse.class)
    public RestResponse listWorkReportsVal(ListWorkReportsValCommand cmd) {
        ListWorkReportsValResponse res = workReportService.listWorkReportsVal(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /workReport/getWorkReportVal</b>
     * <p>3-4.获取工作汇报详情 </p>
     */
    @RequestMapping("getWorkReportVal")
    @RestReturn(value=WorkReportValDTO.class)
    public RestResponse getWorkReportVal(WorkReportValIdCommand cmd) {
        WorkReportValDTO res = workReportService.getWorkReportVal(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
