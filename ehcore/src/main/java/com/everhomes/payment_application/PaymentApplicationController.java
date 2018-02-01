package com.everhomes.payment_application;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.payment_application.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ying.xiong on 2017/12/27.
 */
@RestDoc(value="payment_application form controller", site="core")
@RestController
@RequestMapping("/payment_application")
public class PaymentApplicationController extends ControllerBase {

    @Autowired
    private PaymentApplicationService paymentApplicationService;
    /**
     * <p>创建付款请示单</p>
     * <b>URL: /payment_application/createPaymentApplication</b>
     */
    @RequestMapping("createPaymentApplication")
    @RestReturn(PaymentApplicationDTO.class)
    public RestResponse createPaymentApplication(CreatePaymentApplicationCommand cmd){
        return new RestResponse(paymentApplicationService.createPaymentApplication(cmd));
    }

    /**
     * <p>付款请示单列表</p>
     * <b>URL: /payment_application/searchPaymentApplications</b>
     */
    @RequestMapping("searchPaymentApplications")
    @RestReturn(SearchPaymentApplicationResponse.class)
    public RestResponse searchPaymentApplications(SearchPaymentApplicationCommand cmd){
        return new RestResponse(paymentApplicationService.searchPaymentApplications(cmd));
    }

    /**
     * <p>查看付款请示单</p>
     * <b>URL: /payment_application/getPaymentApplication</b>
     */
    @RequestMapping("getPaymentApplication")
    @RestReturn(PaymentApplicationDTO.class)
    public RestResponse getPaymentApplication(GetPaymentApplicationCommand cmd){
        return new RestResponse(paymentApplicationService.getPaymentApplication(cmd));
    }

    /**
     * <p>生成单号</p>
     * <b>URL: /payment_application/generatePaymentApplicationNumber</b>
     */
    @RequestMapping("generatePaymentApplicationNumber")
    @RestReturn(String.class)
    public RestResponse generatePaymentApplicationNumber(){
        return new RestResponse(paymentApplicationService.generatePaymentApplicationNumber());
    }
}
