package com.everhomes.decoration;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.general_approval.GeneralApprovalService;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.decoration.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static org.apache.poi.hslf.record.RecordTypes.List;

@RestDoc(value = "decoration controller", site = "ehcore")
@RestController
@RequestMapping("/decoration")
public class DecorationController extends ControllerBase {

    @Autowired
    private DecorationService decorationService;
    public static final Long moduleId = 43000L;
    public static final String moduleType = "decoration";
    @Autowired
    private GeneralFormService generalFormService;
    @Autowired
    private GeneralApprovalService generalApprovalService;

    /**
     * <b>URL: /decoration/getUserMemberGroup</b>
     * <p>
     * 查询用户类型
     * </p>
     */
    @RequestMapping("getUserMemberGroup")
    @RestReturn(GetUserMemberGroupResponse.class)
    public RestResponse getUserMemberGroup(@Valid GetUserMemberGroupCommand cmd) {
        GetUserMemberGroupResponse res = this.decorationService.getUserMemberGroup(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/listUserRequests</b>
     * <p>
     * 查询用户装修申请列表
     * </p>
     */
    @RequestMapping("listUserRequests")
    @RestReturn(SearchRequestResponse.class)
    public RestResponse listUserRequests(@Valid ListUserRequestsCommand cmd) {
        SearchRequestResponse res = this.decorationService.searchUserRelateRequest(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/getBaseSetting</b>
     * <p>
     * 获取基础设置
     * </p>
     */
    @RequestMapping("getBaseSetting")
    @RestReturn(DecorationIllustrationDTO.class)
    public RestResponse getBaseSetting(@Valid GetIlluStrationCommand cmd) {
        cmd.setOwnerType(IllustrationType.BASIC.getCode());
        DecorationIllustrationDTO dto = this.decorationService.getIllustration(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/createRequest</b>
     * <p>
     * 新建装修申请
     * </p>
     */
    @RequestMapping("createRequest")
    @RestReturn(DecorationRequestDTO.class)
    public RestResponse createRequest(@Valid CreateRequestCommand cmd) {
        DecorationRequestDTO dto = this.decorationService.createRequest(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/listDecorationCompanies</b>
     * <p>
     * 列出装修公司
     * </p>
     */
    @RequestMapping("listDecorationCompanies")
    @RestReturn(value=DecorationCompanyDTO.class, collection=true)
    public RestResponse listDecorationCompanies(@Valid ListDecorationCompaniesCommand cmd) {
        List<DecorationCompanyDTO> list =  this.decorationService.listDecorationCompanies(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/getDecorationDetail</b>
     * <p>
     * 获取装修详情
     * </p>
     */
    @RequestMapping("getDecorationDetail")
    @RestReturn(value=DecorationRequestDTO.class)
    public RestResponse getDecorationDetail(@Valid GetDecorationDetailCommand cmd) {
        DecorationRequestDTO dto = this.decorationService.getRequestDetail(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/listWorkers</b>
     * <p>
     * 查询装修人员列表
     * </p>
     */
    @RequestMapping("listWorkers")
    @RestReturn(ListWorkersResponse.class)
    public RestResponse listWorkers(@Valid ListWorkersCommand cmd) {
        ListWorkersResponse res =  decorationService.listWorkers(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/getFileSetting</b>
     * <p>
     * 获取装修资料说明
     * </p>
     */
    @RequestMapping("getFileSetting")
    @RestReturn(DecorationIllustrationDTO.class)
    public RestResponse getFileSetting(@Valid GetIlluStrationCommand cmd) {
        cmd.setOwnerType(IllustrationType.FILE.getCode());
        DecorationIllustrationDTO dto = this.decorationService.getIllustration(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/getFee</b>
     * <p>
     * 查看费用清单
     * </p>
     */
    @RequestMapping("getFee")
    @RestReturn(GetDecorationFeeResponse.class)
    public RestResponse getFee(@Valid RequestIdCommand cmd) {
        GetDecorationFeeResponse res = this.decorationService.getFeeInfo(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/getLicense</b>
     * <p>
     * 获取装修证明
     * </p>
     */
    @RequestMapping("getLicense")
    @RestReturn(DecorationLicenseDTO.class)
    public RestResponse getLicense(@Valid GetLicenseCommand cmd) {
        DecorationLicenseDTO dto = this.decorationService.getLicense(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/geQrDetail</b>
     * <p>
     * 获取扫码后详情
     * </p>
     */
    @RequestMapping("geQrDetail")
    @RestReturn(QrDetailDTO.class)
    @RequireAuthentication(value = false)
    public RestResponse geQrDetail(@Valid GetLicenseCommand cmd) {
        QrDetailDTO dto = this.decorationService.getQrDetail(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/getApplySetting</b>
     * <p>
     * 获取施工申请说明
     * </p>
     */
    @RequestMapping("getApplySetting")
    @RestReturn(DecorationIllustrationDTO.class)
    public RestResponse getApplySetting(@Valid GetIlluStrationCommand cmd) {
        cmd.setOwnerType(IllustrationType.APPLY.getCode());
        DecorationIllustrationDTO dto = this.decorationService.getIllustration(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/getFormTemplateByFormId</b>
     * <p>
     * 通过id获取表单
     * </p>
     */
    @RequestMapping("getFormTemplateByFormId")
    @RestReturn(GeneralFormDTO.class)
    public RestResponse getFormTemplateByFormId(@Valid GetTemplateByFormIdCommand cmd) {
        GeneralFormDTO dto = generalFormService.getTemplateByFormId(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/getDecorationApprovals</b>
     * <p>
     * 获取审批
     * </p>
     */
    @RequestMapping("getDecorationApprovals")
    @RestReturn(ListGeneralApprovalResponse.class)
    public RestResponse getDecorationApprovals(@Valid ListGeneralApprovalCommand cmd) {
        cmd.setStatus(GeneralApprovalStatus.RUNNING.getCode());
        ListGeneralApprovalResponse result = generalApprovalService.listGeneralApproval(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/getApprovalVals</b>
     * <p>
     * 获取指定审批详情
     * </p>
     */
    @RequestMapping("getApprovalVals")
    @RestReturn(DecorationFlowCaseDTO.class)
    public RestResponse getApprovalVals(@Valid getApprovalValsCommand cmd) {
        DecorationFlowCaseDTO dto = this.decorationService.getApprovalVals(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/getCompleteSetting</b>
     * <p>
     * 	获取竣工申请说明
     * </p>
     */
    @RequestMapping("getCompleteSetting")
    @RestReturn(DecorationIllustrationDTO.class)
    public RestResponse getCompleteSetting(@Valid GetIlluStrationCommand cmd) {
        cmd.setOwnerType(IllustrationType.COMPLETE.getCode());
        DecorationIllustrationDTO dto = this.decorationService.getIllustration(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/getRefoundSetting</b>
     * <p>
     * 	获取押金退回说明
     * </p>
     */
    @RequestMapping("getRefoundSetting")
    @RestReturn(DecorationIllustrationDTO.class)
    public RestResponse getRefoundSetting(@Valid RequestIdCommand cmd) {
        DecorationIllustrationDTO dto = this.decorationService.getRefundInfo(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/completeDecoration</b>
     * <p>
     * 申请竣工
     * </p>
     */
    @RequestMapping("completeDecoration")
    @RestReturn(DecorationFlowCaseDTO.class)
    public RestResponse completeDecoration(@Valid RequestIdCommand cmd) {
        DecorationFlowCaseDTO decorationFlowCaseDTO = this.decorationService.completeDecoration(cmd);
        RestResponse response = new RestResponse(decorationFlowCaseDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/updateWorker</b>
     * <p>
     * 	新增/修改施工人员
     * </p>
     */
    @RequestMapping("updateWorker")
    @RestReturn(DecorationWorkerDTO.class)
    public RestResponse updateWorker(@Valid UpdateWorkerCommand cmd) {
        DecorationWorkerDTO dto = decorationService.updateWorker(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/deleteWorker</b>
     * <p>
     * 	删除施工人员
     * </p>
     */
    @RequestMapping("deleteWorker")
    @RestReturn(String.class)
    public RestResponse deleteWorker(@Valid DeleteWorkerCommand cmd) {
        decorationService.deleteWorker(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
