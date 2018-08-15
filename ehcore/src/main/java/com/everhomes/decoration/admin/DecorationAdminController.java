package com.everhomes.decoration.admin;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.decoration.DecorationService;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.decoration.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestDoc(value = "decoration admin controller", site = "ehcore")
@RestController
@RequestMapping("/decoration/admin")
public class DecorationAdminController extends ControllerBase {

    @Autowired
    private DecorationService decorationService;
    /**
     * <b>URL: /decoration/admin/searchRequests</b>
     * <p>
     * 查询装修申请列表
     * </p>
     */
    @RequestMapping("searchRequests")
    @RestReturn(SearchRequestResponse.class)
    public RestResponse searchRequests(@Valid SearchRequestsCommand cmd) {
        SearchRequestResponse res = this.decorationService.searchRequest(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/exportRequests</b>
     * <p>
     * 导出装修申请列表
     * </p>
     */
    @RequestMapping("exportRequests")
    public void exportRequests(@Valid SearchRequestsCommand cmd, HttpServletResponse response) {
        this.decorationService.exportRequests(cmd,response);
    }

    /**
     * <b>URL: /decoration/admin/listWorkers</b>
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
     * <b>URL: /decoration/admin/exportWorkers</b>
     * <p>
     * 导出装修人员列表
     * </p>
     */
    @RequestMapping("exportWorkers")
    public void exportWorkers(@Valid ListWorkersCommand cmd, HttpServletResponse response) {
        this.decorationService.exportWorkers(cmd,response);
    }

    /**
     * <b>URL: /decoration/admin/getDecorationDetail</b>
     * <p>
     * 获取装修详情
     * </p>
     */
    @RequestMapping("getDecorationDetail")
    @RestReturn(DecorationRequestDTO.class)
    public RestResponse getDecorationDetail(@Valid GetDecorationDetailCommand cmd) {
        cmd.setProcessorType(ProcessorType.ROOT.getCode());
        DecorationRequestDTO dto = this.decorationService.getRequestDetail(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/cancelRequest</b>
     * <p>
     * 终止装修
     * </p>
     */
    @RequestMapping("cancelRequest")
    @RestReturn(String.class)
    public RestResponse cancelRequest(@Valid RequestIdCommand cmd) {
        this.decorationService.cancelRequest(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/modifyRefoundAmount</b>
     * <p>
     * 填写/修改退款金额
     * </p>
     */
    @RequestMapping("modifyRefoundAmount")
    @RestReturn(String.class)
    public RestResponse modifyRefoundAmount(@Valid ModifyRefoundAmountCommand cmd) {
        this.decorationService.modifyRefoundAmount(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/confirmRefound</b>
     * <p>
     * 确认退款
     * </p>
     */
    @RequestMapping("confirmRefound")
    @RestReturn(String.class)
    public RestResponse confirmRefound(@Valid RequestIdCommand cmd) {
        this.decorationService.confirmRefound(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/modifyFee</b>
     * <p>
     * 填写/修改缴费金额
     * </p>
     */
    @RequestMapping("modifyFee")
    @RestReturn(String.class)
    public RestResponse modifyFee(@Valid ModifyFeeCommand cmd) {
        this.decorationService.modifyFee(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/confirmFee</b>
     * <p>
     * 确认缴费
     * </p>
     */
    @RequestMapping("confirmFee")
    @RestReturn(String.class)
    public RestResponse confirmFee(@Valid RequestIdCommand cmd) {
        this.decorationService.confirmFee(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/getBaseSetting</b>
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
     * <b>URL: /decoration/admin/updateBaseSetting</b>
     * <p>
     * 更新基础设置
     * </p>
     */
    @RequestMapping("updateBaseSetting")
    @RestReturn(String.class)
    public RestResponse updateBaseSetting(@Valid UpdateIllustrationCommand cmd) {
        cmd.setOwnerType(IllustrationType.BASIC.getCode());
        this.decorationService.setIllustration(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/getFileSetting</b>
     * <p>
     * 获取装修资料管理设置
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
     * <b>URL: /decoration/admin/updateFileSetting</b>
     * <p>
     * 修改装修资料管理设置
     * </p>
     */
    @RequestMapping("updateFileSetting")
    @RestReturn(String.class)
    public RestResponse updateFileSetting(@Valid UpdateIllustrationCommand cmd) {
        cmd.setOwnerType(IllustrationType.FILE.getCode());
        this.decorationService.setIllustration(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/getFeeSetting</b>
     * <p>
     * 获取缴费管理设置
     * </p>
     */
    @RequestMapping("getFeeSetting")
    @RestReturn(DecorationIllustrationDTO.class)
    public RestResponse getFeeSetting(@Valid GetIlluStrationCommand cmd) {
        cmd.setOwnerType(IllustrationType.FEE.getCode());
        DecorationIllustrationDTO dto = this.decorationService.getIllustration(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/updateFeeSetting</b>
     * <p>
     * 更新缴费管理设置
     * </p>
     */
    @RequestMapping("updateFeeSetting")
    @RestReturn(String.class)
    public RestResponse updateFeeSetting(@Valid UpdateIllustrationCommand cmd) {
        cmd.setOwnerType(IllustrationType.FEE.getCode());
        this.decorationService.setIllustration(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/getApplySetting</b>
     * <p>
     * 获取施工申请说明设置
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
     * <b>URL: /decoration/admin/updateApplySetting</b>
     * <p>
     * 修改施工申请说明设置
     * </p>
     */
    @RequestMapping("updateApplySetting")
    @RestReturn(String.class)
    public RestResponse updateApplySetting(@Valid UpdateApplySettingCommand cmd) {
        this.decorationService.setApplySetting(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/getCompleteSetting</b>
     * <p>
     * 	获取竣工申请设置
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
     * <b>URL: /decoration/admin/updateCompleteSetting</b>
     * <p>
     * 修改竣工申请设置
     * </p>
     */
    @RequestMapping("updateCompleteSetting")
    @RestReturn(String.class)
    public RestResponse updateCompleteSetting(@Valid UpdateIllustrationCommand cmd) {
        cmd.setOwnerType(IllustrationType.COMPLETE.getCode());
        this.decorationService.setIllustration(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/getRefoundSetting</b>
     * <p>
     * 	获取押金退回设置
     * </p>
     */
    @RequestMapping("getRefoundSetting")
    @RestReturn(DecorationIllustrationDTO.class)
    public RestResponse getRefoundSetting(@Valid GetIlluStrationCommand cmd) {
        cmd.setOwnerType(IllustrationType.REFOUND.getCode());
        DecorationIllustrationDTO dto = this.decorationService.getIllustration(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /decoration/admin/updateRefoundSetting</b>
     * <p>
     * 更新押金退回设置
     * </p>
     */
    @RequestMapping("updateRefoundSetting")
    @RestReturn(String.class)
    public RestResponse updateRefoundSetting(@Valid UpdateIllustrationCommand cmd) {
        cmd.setOwnerType(IllustrationType.REFOUND.getCode());
        this.decorationService.setIllustration(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
