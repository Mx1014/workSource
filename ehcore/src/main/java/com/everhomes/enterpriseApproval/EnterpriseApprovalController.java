package com.everhomes.enterpriseApproval;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterpriseApproval.ApprovalFlowIdCommand;
import com.everhomes.rest.enterpriseApproval.ApprovalFlowIdsCommand;
import com.everhomes.rest.enterpriseApproval.CreateApprovalTemplatesCommand;
import com.everhomes.rest.enterpriseApproval.CreateEnterpriseApprovalCommand;
import com.everhomes.rest.enterpriseApproval.DeliverApprovalFlowCommand;
import com.everhomes.rest.enterpriseApproval.DeliverApprovalFlowsCommand;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalDTO;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalGroupDTO;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalIdCommand;
import com.everhomes.rest.enterpriseApproval.ListApprovalFlowRecordsCommand;
import com.everhomes.rest.enterpriseApproval.ListApprovalFlowRecordsResponse;
import com.everhomes.rest.enterpriseApproval.ListEnterpriseApprovalsCommand;
import com.everhomes.rest.enterpriseApproval.ListEnterpriseApprovalsResponse;
import com.everhomes.rest.enterpriseApproval.UpdateEnterpriseApprovalCommand;
import com.everhomes.rest.enterpriseApproval.VerifyApprovalTemplatesCommand;
import com.everhomes.rest.enterpriseApproval.VerifyApprovalTemplatesResponse;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/enterpriseApproval")
public class EnterpriseApprovalController extends ControllerBase{

    public static final Long MODULE_ID = 52000L;

    public static final String MODULE_TYPE = "any-module";

    //  The symbol of the approval is 'GENERAL_APPROVAL' because of the history
    public static final String APPROVAL_OWNER_TYPE = "GENERAL_APPROVAL";

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
     * <b>URL: /enterpriseApproval/listActiveApprovalFlowRecords</b>
     * <p>OA 流程监控</p>
     */
    @RequestMapping("listActiveApprovalFlowRecords")
    @RestReturn(value = ListApprovalFlowRecordsResponse.class)
    public RestResponse listActiveApprovalFlowRecords(@Valid ListApprovalFlowRecordsCommand cmd){
        ListApprovalFlowRecordsResponse res = enterpriseApprovalService.listActiveApprovalFlowRecords(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/stopApprovalFlows</b>
     * <p>终止流程</p>
     */
    @RequestMapping("stopApprovalFlows")
    @RestReturn(value = String.class)
    public RestResponse stopApprovalFlows(@Valid ApprovalFlowIdsCommand cmd){
        enterpriseApprovalService.stopApprovalFlows(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/deleteApprovalFlow</b>
     * <p>删除流程</p>
     */
    @RequestMapping("deleteApprovalFlow")
    @RestReturn(value = String.class)
    public RestResponse deleteApprovalFlow(@Valid ApprovalFlowIdCommand cmd) {
        enterpriseApprovalService.deleteApprovalFlow(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/listApprovalProcessors</b>
     * <p>获取原始审批人</p>
     */
    @RequestMapping("listApprovalProcessors")
    @RestReturn(value = OrganizationMemberDTO.class, collection = true)
    public RestResponse listApprovalProcessors(@Valid ApprovalFlowIdCommand cmd){
        List<OrganizationMemberDTO> res = enterpriseApprovalService.listApprovalProcessors(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/deliverApprovalFlow</b>
     * <p>移交单个流程</p>
     */
    @RequestMapping("deliverApprovalFlow")
    @RestReturn(value = String.class)
    public RestResponse deliverApprovalFlow(@Valid DeliverApprovalFlowCommand cmd){
        enterpriseApprovalService.deliverApprovalFlow(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/deliverApprovalFlows</b>
     * <p>批量移交流程</p>
     */
    @RequestMapping("deliverApprovalFlows")
    @RestReturn(value = String.class)
    public RestResponse deliverApprovalFlows(@Valid DeliverApprovalFlowsCommand cmd){
        enterpriseApprovalService.deliverApprovalFlows(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/verifyApprovalTemplates</b>
     * <p> 判断是否需要创建审批模板 </p>
     */
    @RequestMapping("verifyApprovalTemplates")
    @RestReturn(value=VerifyApprovalTemplatesResponse.class)
    public RestResponse verifyApprovalTemplates(@Valid VerifyApprovalTemplatesCommand cmd) {
        VerifyApprovalTemplatesResponse res = enterpriseApprovalService.verifyApprovalTemplates(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }


    /**
     * <b>URL: /enterpriseApproval/createApprovalTemplates</b>
     * <p> 创建审批模板 </p>
     */
    @RequestMapping("createApprovalTemplates")
    @RestReturn(value=String.class)
    public RestResponse createApprovalTemplates(@Valid CreateApprovalTemplatesCommand cmd) {
        enterpriseApprovalService.createApprovalTemplates(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/listEnterpriseApprovalGroups</b>
     * <p>OA 审批分类列表(组列表)</p>
     */
    @RequestMapping("listEnterpriseApprovalGroups")
    @RestReturn(value = EnterpriseApprovalGroupDTO.class, collection = true)
    public RestResponse listEnterpriseApprovalGroups(){
        List<EnterpriseApprovalGroupDTO> res = enterpriseApprovalService.listEnterpriseApprovalGroups();
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/createEnterpriseApproval</b>
     * <p>OA 新增审批</p>
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
     * <b>URL: /enterpriseApproval/deleteEnterpriseApproval</b>
     * <p>OA 删除审批</p>
     */
    @RequestMapping("deleteEnterpriseApproval")
    @RestReturn(value=String.class)
    public RestResponse deleteEnterpriseApproval(@Valid EnterpriseApprovalIdCommand cmd) {
        enterpriseApprovalService.deleteEnterpriseApproval(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/updateEnterpriseApproval</b>
     * <p>OA 编辑审批</p>
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
     * <p>OA 获取审批类型列表(用来做filter条件)</p>
     */
    @RequestMapping("listEnterpriseApprovalTypes")
    @RestReturn(value=ListEnterpriseApprovalsResponse.class)
    public RestResponse listEnterpriseApprovalTypes(@Valid ListEnterpriseApprovalsCommand cmd) {
        ListEnterpriseApprovalsResponse res = enterpriseApprovalService.listEnterpriseApprovalTypes(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/enableEnterpriseApproval</b>
     * <p>OA 启用审批</p>
     */
    @RequestMapping("enableEnterpriseApproval")
    @RestReturn(value=String.class)
    public RestResponse enableEnterpriseApproval(@Valid EnterpriseApprovalIdCommand cmd) {
        enterpriseApprovalService.enableEnterpriseApproval(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseApproval/disableEnterpriseApproval</b>
     * <p>OA 禁用审批</p>
     */
    @RequestMapping("disableEnterpriseApproval")
    @RestReturn(value=String.class)
    public RestResponse disableEnterpriseApproval(@Valid EnterpriseApprovalIdCommand cmd) {
        enterpriseApprovalService.disableEnterpriseApproval(cmd);
        RestResponse response = new RestResponse();
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
