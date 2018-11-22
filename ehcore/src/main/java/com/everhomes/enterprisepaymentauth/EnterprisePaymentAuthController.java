package com.everhomes.enterprisepaymentauth;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterprisepaymentauth.GetEmployeePaymentAuthDetailCommand;
import com.everhomes.rest.enterprisepaymentauth.GetEmployeePaymentAuthDetailResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEmployeePaymentLimitChangeLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEmployeePaymentLimitChangeLogsResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEmployeePaymentLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEmployeePaymentLogsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/enterprisepaymentauth")
public class EnterprisePaymentAuthController extends ControllerBase {
    @Autowired
    private EnterprisePaymentAuthService enterprisePaymentAuthService;

    /**
     * <p>获取员工应用场景授权详情</p>
     * <b>URL: /enterprisepaymentauth/getEmployeePaymentAuthDetail</b>
     */
    @RequestMapping("getEmployeePaymentAuthDetail")
    @RestReturn(GetEmployeePaymentAuthDetailResponse.class)
    public RestResponse getEmployeePaymentAuthDetail(GetEmployeePaymentAuthDetailCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.getEmployeePaymentAuthDetail(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取企业员工的支付记录</p>
     * <b>URL: /enterprisepaymentauth/listEmployeePaymentLogs</b>
     */
    @RequestMapping("listEmployeePaymentLogs")
    @RestReturn(ListEmployeePaymentLogsResponse.class)
    public RestResponse listEmployeePaymentLogs(ListEmployeePaymentLogsCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.listEmployeePaymentLogs(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取企业员工支付额度调额记录</p>
     * <b>URL: /enterprisepaymentauth/listEmployeePaymentLimitChangeLogs</b>
     */
    @RequestMapping("listEmployeePaymentLimitChangeLogs")
    @RestReturn(ListEmployeePaymentLimitChangeLogsResponse.class)
    public RestResponse listEmployeePaymentLimitChangeLogs(ListEmployeePaymentLimitChangeLogsCommand cmd) {
        RestResponse response = new RestResponse(enterprisePaymentAuthService.listEmployeePaymentLimitChangeLogs(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
