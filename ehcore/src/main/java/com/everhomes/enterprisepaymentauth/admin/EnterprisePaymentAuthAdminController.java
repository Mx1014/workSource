// @formatter:off
package com.everhomes.enterprisepaymentauth.admin;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.enterprisepaymentauth.EnterprisePaymentAuthService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterprisepaymentauth.BatchCreateOrUpdateEmployeePaymentLimitCommand;
import com.everhomes.rest.enterprisepaymentauth.CreateOrUpdateEnterprisePaymentSceneLimitCommand;
import com.everhomes.rest.enterprisepaymentauth.ExportEnterprisePaymentPayLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.GetEmployeePaymentAuthStatusCommand;
import com.everhomes.rest.enterprisepaymentauth.GetEmployeePaymentAuthStatusResponse;
import com.everhomes.rest.enterprisepaymentauth.GetEnterpriseEmployeePaymentLimitDetailCommand;
import com.everhomes.rest.enterprisepaymentauth.GetEnterpriseEmployeePaymentLimitDetailResponse;
import com.everhomes.rest.enterprisepaymentauth.GetEnterprisePaymentAuthInfoCommand;
import com.everhomes.rest.enterprisepaymentauth.GetEnterprisePaymentAuthInfoResponse;
import com.everhomes.rest.enterprisepaymentauth.GetEnterprisePaymentSceneLimitInfoCommand;
import com.everhomes.rest.enterprisepaymentauth.GetEnterprisePaymentSceneLimitInfoResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterpriseEmployeePaymentLimitChangeLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterpriseEmployeePaymentLimitChangeLogsResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentAuthOfEmployeesCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentAuthOfEmployeesResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentAuthOperateLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentAuthOperateLogsResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentPayLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentPayLogsResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentSceneEmployeeLimitCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentSceneEmployeeLimitResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentScenesCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentScenesResponse;
import com.everhomes.rest.enterprisepaymentauth.ListPaymentScenesResponse;
import com.everhomes.rest.enterprisepaymentauth.PaymentAuthCheckAndFrozenCommand;
import com.everhomes.rest.enterprisepaymentauth.PaymentAuthCheckAndFrozenResponse;
import com.everhomes.rest.enterprisepaymentauth.PaymentAuthFrozenConfirmCommand;
import com.everhomes.rest.enterprisepaymentauth.PaymentAuthUnFrozenCommand;
import com.everhomes.rest.enterprisepaymentauth.TestPaymentCallbackCommand;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/enterprisepaymentauth")
public class EnterprisePaymentAuthAdminController extends ControllerBase {

    @Autowired
    private EnterprisePaymentAuthService enterprisePaymentAuthService;

    /**
     * <p>企业支付授权-额度统计信息查询</p>
     * <b>URL: /admin/enterprisepaymentauth/getPaymentAuthInfo</b>
     */
    @RequestMapping("getPaymentAuthInfo")
    @RestReturn(GetEnterprisePaymentAuthInfoResponse.class)
    public RestResponse getEnterprisePaymentAuthInfo(GetEnterprisePaymentAuthInfoCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.getEnterprisePaymentAuthInfo(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取企业支付员工授权列表</p>
     * <b>URL: /admin/enterprisepaymentauth/listPaymentAuthOfEmployees</b>
     */
    @RequestMapping("listPaymentAuthOfEmployees")
    @RestReturn(ListEnterprisePaymentAuthOfEmployeesResponse.class)
    public RestResponse listEnterprisePaymentAuthOfEmployees(ListEnterprisePaymentAuthOfEmployeesCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.listEnterprisePaymentAuthOfEmployees(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>企业支付可用场景列表</p>
     * <b>URL: /admin/enterprisepaymentauth/listPaymentScenes</b>
     */
    @RequestMapping("listPaymentScenes")
    @RestReturn(ListEnterprisePaymentScenesResponse.class)
    public RestResponse listEnterprisePaymentScenes(ListEnterprisePaymentScenesCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.listEnterprisePaymentScenes(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>查询企业支付场景额度信息</p>
     * <b>URL: /admin/enterprisepaymentauth/getPaymentSceneLimitInfo</b>
     */
    @RequestMapping("getPaymentSceneLimitInfo")
    @RestReturn(GetEnterprisePaymentSceneLimitInfoResponse.class)
    public RestResponse getEnterprisePaymentSceneLimitInfo(GetEnterprisePaymentSceneLimitInfoCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.getEnterprisePaymentSceneLimitInfo(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>查询企业支付场景员工授权详细信息</p>
     * <b>URL: /admin/enterprisepaymentauth/listPaymentSceneEmployeeLimit</b>
     */
    @RequestMapping("listPaymentSceneEmployeeLimit")
    @RestReturn(ListEnterprisePaymentSceneEmployeeLimitResponse.class)
    public RestResponse listPaymentSceneEmployeeLimit(ListEnterprisePaymentSceneEmployeeLimitCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.listPaymentSceneEmployeeLimit(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>设置企业支付场景授权额度</p>
     * <b>URL: /admin/enterprisepaymentauth/createOrUpdatePaymentSceneLimit</b>
     */
    @RequestMapping("createOrUpdatePaymentSceneLimit")
    @RestReturn(String.class)
    public RestResponse createOrUpdatePaymentSceneLimit(CreateOrUpdateEnterprisePaymentSceneLimitCommand cmd) {
        enterprisePaymentAuthService.createOrUpdatePaymentSceneLimit(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>批量设置员工授权额度</p>
     * <b>URL: /admin/enterprisepaymentauth/batchCreateOrUpdateEmployeePaymentLimit</b>
     */
    @RequestMapping("batchCreateOrUpdateEmployeePaymentLimit")
    @RestReturn(String.class)
    public RestResponse batchUpdateEnterprisePaymentEmployeeAuth(BatchCreateOrUpdateEmployeePaymentLimitCommand cmd, HttpServletRequest request) {
        enterprisePaymentAuthService.batchCreateOrUpdateEmployeePaymentLimit(cmd, request);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>员工授权详情</p>
     * <b>URL: /admin/enterprisepaymentauth/getEmployeePaymentLimitDetail</b>
     */
    @RequestMapping("getEmployeePaymentLimitDetail")
    @RestReturn(GetEnterpriseEmployeePaymentLimitDetailResponse.class)
    public RestResponse getEmployeePaymentLimitDetail(GetEnterpriseEmployeePaymentLimitDetailCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.getEmployeePaymentLimitDetail(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>员工授权记录</p>
     * <b>URL: /admin/enterprisepaymentauth/listEmployeePaymentLimitChangeLogs</b>
     */
    @RequestMapping("listEmployeePaymentLimitChangeLogs")
    @RestReturn(ListEnterpriseEmployeePaymentLimitChangeLogsResponse.class)
    public RestResponse listEmployeePaymentLimitChangeLogs(ListEnterpriseEmployeePaymentLimitChangeLogsCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.listEmployeePaymentLimitChangeLogs(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>授权日志</p>
     * <b>URL: /admin/enterprisepaymentauth/listPaymentAuthOperateLogs</b>
     */
    @RequestMapping("listPaymentAuthOperateLogs")
    @RestReturn(ListEnterprisePaymentAuthOperateLogsResponse.class)
    public RestResponse listPaymentAuthOperateLogs(ListEnterprisePaymentAuthOperateLogsCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.listPaymentAuthOperateLogs(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>查询支付记录</p>
     * <b>URL: /admin/enterprisepaymentauth/listEnterprisePayLogs</b>
     */
    @RequestMapping("listEnterprisePayLogs")
    @RestReturn(ListEnterprisePaymentPayLogsResponse.class)
    public RestResponse listEnterprisePayLogs(ListEnterprisePaymentPayLogsCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.listEnterprisePayLogs(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>导出支付记录</p>
     * <b>URL: /admin/enterprisepaymentauth/exportEnterprisePayLogs</b>
     */
    @RequestMapping("exportEnterprisePayLogs")
    @RestReturn(String.class)
    public RestResponse exportEnterprisePayLogs(ExportEnterprisePaymentPayLogsCommand cmd) {
        enterprisePaymentAuthService.exportEnterprisePayLogs(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>查询所有可支付场景列表</p>
     * <b>URL: /admin/enterprisepaymentauth/listSimplePaymentScenes</b>
     */
    @RequestMapping("listSimplePaymentScenes")
    @RestReturn(ListPaymentScenesResponse.class)
    public RestResponse listPaymentScenes() {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.listPaymentScenes());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>企业支付授权额度余额信息</p>
     * <b>URL: /admin/enterprisepaymentauth/getPaymentAuthStatus</b>
     */
    @RequestMapping("getPaymentAuthStatus")
    @RestReturn(GetEmployeePaymentAuthStatusResponse.class)
    @RequireAuthentication(false)
    public RestResponse getPaymentAuthStatus(GetEmployeePaymentAuthStatusCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.getPaymentAuthStatus(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>企业支付额度鉴权(冻结)</p>
     * <b>URL: /admin/enterprisepaymentauth/paymentAuthCheckAndFrozen</b>
     */
    @RequestMapping("paymentAuthCheckAndFrozen")
    @RestReturn(PaymentAuthCheckAndFrozenResponse.class)
    @RequireAuthentication(false)
    public RestResponse checkAndPaymentFrozen(PaymentAuthCheckAndFrozenCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.checkAndPaymentFrozen(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/enterprisepaymentauth/paymentAuthFrozenConfirm</b>
     * <p>支付成功调用</p>
     */
    @RequestMapping("paymentAuthFrozenConfirm")
    @RestReturn(value = String.class)
    @RequireAuthentication(false)
    public RestResponse paymentAuthFrozenConfirm(PaymentAuthFrozenConfirmCommand cmd, HttpServletRequest request) {
        enterprisePaymentAuthService.paymentAuthFrozenConfirm(cmd, true, request);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>支付失败或退款调用，取消冻结</p>
     * <b>URL: /admin/enterprisepaymentauth/paymentAuthUnFrozen</b>
     */
    @RequestMapping("paymentAuthUnFrozen")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse paymentAuthUnFrozen(PaymentAuthUnFrozenCommand cmd) {
        enterprisePaymentAuthService.paymentAuthUnFrozen(cmd, true);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/enterprisepaymentauth/testPaymentCallback</b>
     * <p>支付回调</p>
     */
    @RequestMapping("testPaymentCallback")
    @RestReturn(value = String.class)
    @RequireAuthentication(false)
    public RestResponse testPaymentCallback(TestPaymentCallbackCommand cmd, HttpServletRequest request) {
        enterprisePaymentAuthService.testPaymentCallback(cmd, request);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}