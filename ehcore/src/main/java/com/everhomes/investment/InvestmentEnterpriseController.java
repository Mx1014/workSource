package com.everhomes.investment;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.investment.CreateInvestmentCommand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value="investment enterprise controller", site="core")
@RestController
@RequestMapping("/investment")
public class InvestmentEnterpriseController {

    /**
     * <b>URL: /investment/createInvestment</b>
     * <p> 新建招商客户 </p>
     */
    @RequestMapping("createInvestment")
    @RestReturn(value=Long.class)
    public RestResponse createInvestment(CreateInvestmentCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /investment/createInvestment</b>
     * <p> 修改招商客户 </p>
     */
    @RequestMapping("updateInvestment")
    @RestReturn(value=Long.class)
    public RestResponse updateInvestment(CreateInvestmentCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /investment/deleteInvestment</b>
     * <p> 删除招商客户 </p>
     */
    @RequestMapping("deleteInvestment")
    @RestReturn(value=Long.class)
    public RestResponse deleteInvestment(CreateInvestmentCommand cmd) {
        RestResponse response = new RestResponse();
        // cmd.getId() cmd.getNameSpaceId()
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }



}
