package com.everhomes.enterpriseApproval;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterpriseApproval.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/enterpriseApproval")
public class EnterpriseApprovalController extends ControllerBase{

    @Autowired
    private EnterpriseApprovalService enterpriseApprovalService;

    /**
     * <b>URL: /enterpriseApproval/listApprovalFlowRecords</b>
     * <p>OA 流程记录(流程查询)</p>
     */
    @RequestMapping("listApprovalFlowRecords")
    @RestReturn(value = ListApprovalFlowRecordsResponse.class)
    public RestResponse listApprovalFlowRecords(@Valid ListApprovalFlowRecordsCommand cmd){
        ListApprovalFlowRecordsResponse res = enterpriseApprovalService.listApprovalFlowRecords(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/exportApprovalFlowRecords</b>
     * <p>OA 导出流程记录</p>
     */
    @RequestMapping("exportApprovalFlowRecords")
    @RestReturn(value = String.class)
    public RestResponse exportApprovalFlowRecords(@Valid ListApprovalFlowRecordsCommand cmd){
        enterpriseApprovalService.exportApprovalFlowRecords(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/listApprovalFlowMonitors</b>
     * <p>OA 流程监控</p>
     */
    @RequestMapping("listApprovalFlowMonitors")
    @RestReturn(value = ListApprovalFlowRecordsResponse.class)
    public RestResponse listApprovalFlowMonitors(@Valid ListApprovalFlowRecordsCommand cmd){
        ListApprovalFlowRecordsResponse res = enterpriseApprovalService.listApprovalFlowMonitors(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/listAvailableApprovalGroups</b>
     * <p>OA 流程监控</p>
     */
    @RequestMapping("listAvailableApprovalGroups")
    @RestReturn(value = EnterpriseApprovalGroupDTO.class, collection = true)
    public RestResponse listAvailableApprovalGroups(){
        List<EnterpriseApprovalGroupDTO> res = enterpriseApprovalService.listAvailableApprovalGroups();
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/createEnterpriseApproval</b>
     * <p>OA 新增审批模板</p>
     * @return
     */
    @RequestMapping("createEnterpriseApproval")
    @RestReturn(value=EnterpriseApprovalDTO.class)
    public RestResponse createEnterpriseApproval(@Valid CreateEnterpriseApprovalCommand cmd) {
        EnterpriseApprovalDTO res = enterpriseApprovalService.createEnterpriseApproval(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/updateEnterpriseApproval</b>
     * <p>OA 编辑审批模板</p>
     * @return
     */
    @RequestMapping("updateEnterpriseApproval")
    @RestReturn(value=EnterpriseApprovalDTO.class)
    public RestResponse updateEnterpriseApproval(@Valid UpdateEnterpriseApprovalCommand cmd) {
        EnterpriseApprovalDTO res = enterpriseApprovalService.updateEnterpriseApproval(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/listEnterpriseApprovalTypes</b>
     * <p>OA 获取审批类型列表</p>
     * @return
     */
    @RequestMapping("listEnterpriseApprovalTypes")
    @RestReturn(value=EnterpriseApprovalDTO.class)
    public RestResponse listEnterpriseApprovalTypes(@Valid ListEnterpriseApprovalsCommand cmd) {
        ListEnterpriseApprovalsResponse res = enterpriseApprovalService.listEnterpriseApprovalTypes(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/listEnterpriseApprovals</b>
     * <p>OA 审批列表</p>
     */
    @RequestMapping("listEnterpriseApprovals")
    @RestReturn(value = ListEnterpriseApprovalsResponse.class)
    public RestResponse listEnterpriseApprovals(@Valid ListEnterpriseApprovalsCommand cmd){
        ListEnterpriseApprovalsResponse res = enterpriseApprovalService.listEnterpriseApprovals(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/listAvailableEnterpriseApprovals</b>
     * <p>OA 可见的审批列表</p>
     */
    @RequestMapping("listAvailableEnterpriseApprovals")
    @RestReturn(value = ListEnterpriseApprovalsResponse.class)
    public RestResponse listAvailableEnterpriseApprovals(@Valid ListEnterpriseApprovalsCommand cmd){
        ListEnterpriseApprovalsResponse res = enterpriseApprovalService.listAvailableEnterpriseApprovals(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
