package com.everhomes.enterprise_approval;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterprise_approval.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/enterprise_approval")
public class EnterpriseApprovalController extends ControllerBase{

    @Autowired
    private EnterpriseApprovalService enterpriseApprovalService;

    /**
     * <b>URL: /enterprise_approval/listApprovalFlowRecords</b>
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
     * <b>URL: /enterprise_approval/listApprovalFlowMonitors</b>
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
     * <b>URL: /enterprise_approval/createEnterpriseApproval</b>
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
     * <b>URL: /enterprise_approval/updateEnterpriseApproval</b>
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
     * <b>URL: /enterprise_approval/listEnterpriseApprovals</b>
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



}
