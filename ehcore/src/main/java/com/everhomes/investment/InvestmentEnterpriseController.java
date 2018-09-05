package com.everhomes.investment;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.CustomerController;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.customer.ImportEnterpriseCustomerDataCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.investment.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestDoc(value="investment enterprise controller", site="core")
@RestController
@RequestMapping("/investment")
public class InvestmentEnterpriseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentEnterpriseController.class);

    @Autowired
    private InvestmentEnterpriseService investmentEnterpriseService;

    /**
     * <b>URL: /investment/createInvestment</b>
     * <p> 新建招商客户 </p>
     */
    @RequestMapping("createInvestment")
    @RestReturn(value=Long.class)
    public RestResponse createInvestment(CreateInvestmentCommand cmd) {
        investmentEnterpriseService.createInvestment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /investment/updateInvestment</b>
     * <p> 修改招商客户 </p>
     */
    @RequestMapping("updateInvestment")
    @RestReturn(value=Long.class)
    public RestResponse updateInvestment(CreateInvestmentCommand cmd) {
        investmentEnterpriseService.updateInvestment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /investment/listInvestments</b>
     * <p> 列出招商客户 </p>
     */
    @RequestMapping("listInvestment")
    @RestReturn(value=Long.class)
    public RestResponse listInvestment(SearchEnterpriseCustomerCommand cmd) {
        SearchInvestmentResponse searchInvestmentResponse =  investmentEnterpriseService.listInvestment(cmd);
        RestResponse response = new RestResponse(searchInvestmentResponse);
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
        investmentEnterpriseService.deleteInvestment(cmd);
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /investment/viewInvestmentDetail</b>
     * <p> 查看招商客户 </p>
     */
    @RequestMapping("viewInvestmentDetail")
    @RestReturn(value=EnterpriseInvestmentDTO.class)
    public RestResponse viewInvestmentDetail(ViewInvestmentDetailCommand cmd) {
        // cmd.getId() cmd.getNameSpaceId()
        EnterpriseInvestmentDTO dto = investmentEnterpriseService.viewInvestmentDetail(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /investment/changeInvestmentToCustomer</b>
     * <p> 转换招商客户为已成交客户 </p>
     */
    @RequestMapping("changeInvestmentToCustomer")
    @RestReturn(value=String.class)
    public RestResponse changeInvestmentToCustomer(ChangeInvestmentToCustomerCommand cmd) {
        //createCustomerEntryInfo
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /investment/changeApplyToInvestment</b>
     * <p> 转换申请客户为招商客户 </p>
     */
    @RequestMapping("changeApplyToInvestment")
    @RestReturn(value=String.class)
    public RestResponse changeApplyToInvestment(ChangeApplyToInvestmentCommand cmd) {

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /investment/importInvestmentEnterpriseData</b>
     * <p> 导入招商客户 </p>
     */
    @RequestMapping("importInvestmentEnterpriseData")
    @RestReturn(value=ImportFileTaskDTO.class)
    public RestResponse importInvestmentEnterpriseData(@Valid importInvestmentEnterpriseDataCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        User manaUser = UserContext.current().getUser();
        Long userId = manaUser.getId();
        if (null == files || null == files[0]) {
            LOGGER.error("files is null。userId=" + userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }




}
