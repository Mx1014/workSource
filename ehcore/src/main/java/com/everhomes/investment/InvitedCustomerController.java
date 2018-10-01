package com.everhomes.investment;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.customer.ExportEnterpriseCustomerCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.investment.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.varField.ImportFieldExcelCommand;
import com.everhomes.rest.varField.ListFieldGroupCommand;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestDoc(value="investment enterprise controller", site="core")
@RestController
@RequestMapping("/invitedCustomer")
public class InvitedCustomerController extends ControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvitedCustomerController.class);


    //investment_promotion

    @Autowired
    private InvitedCustomerService invitedCustomerService;

    /**
     * <b>URL: /invitedCustomer/createInvestment</b>
     * <p> 新建招商客户 </p>
     */
    @RequestMapping("createInvestment")
    @RestReturn(value=Long.class)
    public RestResponse createInvestment(CreateInvitedCustomerCommand cmd) {
        invitedCustomerService.createInvitedCustomer(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /invitedCustomer/updateInvestment</b>
     * <p> 修改招商客户 </p>
     */
    @RequestMapping("updateInvestment")
    @RestReturn(value=Long.class)
    public RestResponse updateInvestment(CreateInvitedCustomerCommand cmd) {
        invitedCustomerService.updateInvestment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /invitedCustomer/listInvestment</b>
     * <p> 列出招商客户 </p>
     */
    @RequestMapping("listInvestment")
    @RestReturn(value=Long.class)
    public RestResponse listInvestment(SearchEnterpriseCustomerCommand cmd) {
        SearchInvestmentResponse searchInvestmentResponse =  invitedCustomerService.listInvestment(cmd);
        RestResponse response = new RestResponse(searchInvestmentResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /invitedCustomer/deleteInvestment</b>
     * <p> 删除招商客户 </p>
     */
    @RequestMapping("deleteInvestment")
    @RestReturn(value=Long.class)
    public RestResponse deleteInvestment(DeleteInvitedCustomerCommand cmd) {
        RestResponse response = new RestResponse();
        // cmd.getId() cmd.getNameSpaceId()
        invitedCustomerService.deleteInvestment(cmd);
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /invitedCustomer/viewInvitedCustomerDetail</b>
     * <p> 查看招商客户 </p>
     */
    @RequestMapping("viewInvitedCustomerDetail")
    @RestReturn(value=InvitedCustomerDTO.class)
    public RestResponse viewInvitedCustomerDetail(ViewInvestmentDetailCommand cmd) {
        // cmd.getId() cmd.getNameSpaceId()
        InvitedCustomerDTO dto = invitedCustomerService.viewInvestmentDetail(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /invitedCustomer/changeInvestmentToCustomer</b>
     * <p> 转换招商客户为已成交客户 </p>
     */
    @RequestMapping("changeInvestmentToCustomer")
    @RestReturn(value=Long.class, collection = true)
    public RestResponse changeInvestmentToCustomer(ChangeInvestmentToCustomerCommand cmd) {
        List<Long> customerIds = invitedCustomerService.changeInvestmentToCustomer(cmd);
        RestResponse response = new RestResponse(customerIds);
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /invitedCustomer/changeApplyToInvestment</b>
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
     * <b>URL: /invitedCustomer/importInvestmentEnterpriseData</b>
     * <p> 导入招商客户 </p>
     */
    @RequestMapping("importInvestmentEnterpriseData")
    @RestReturn(value=ImportFileTaskDTO.class)
    public RestResponse importInvestmentEnterpriseData(ImportFieldExcelCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        User manaUser = UserContext.current().getUser();
        Long userId = manaUser.getId();
        if (null == files || null == files[0]) {
            LOGGER.error("files is null。userId=" + userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }

        RestResponse response = new RestResponse(invitedCustomerService.importEnterpriseCustomer(cmd, files[0]));
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /invitedCustomer/exportEnterpriseCustomerTemplate</b>
     * <p>导出企业客户excel模板</p>
     */
    @RequestMapping("exportEnterpriseCustomerTemplate")
    public void exportEnterpriseCustomerTemplate(ListFieldGroupCommand cmd, HttpServletResponse response) {
        invitedCustomerService.exportEnterpriseCustomerTemplate(cmd, response);
    }


    /**
     * <b>URL: /invitedCustomer/exportInvitedCustomer</b>
     * <p>导出企业客户excel</p>
     */
    @RequestMapping("exportInvitedCustomer")
    @RestReturn(value = String.class)
    public RestResponse exportInvitedCustomer(@Valid ExportEnterpriseCustomerCommand cmd) {
        invitedCustomerService.exportContractListByContractList(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /invitedCustomer/giveUpInvitedCustomer</b>
     * <p> 放弃招商客户 </p>
     */
    @RequestMapping("giveUpInvitedCustomer")
    @RestReturn(value=ImportFileTaskDTO.class)
    public RestResponse giveUpInvitedCustomer(@Valid ViewInvestmentDetailCommand cmd) {
        invitedCustomerService.giveUpInvitedCustomer(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /invitedCustomer/changeCustomerAptitude</b>
     * <p>一键转为资质客户</p>
     */
    @RequestMapping("changeCustomerAptitude")
    @RestReturn(value = String.class)
    public RestResponse changeCustomerAptitude(@Valid SearchEnterpriseCustomerCommand cmd) {
        invitedCustomerService.changeCustomerAptitude(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
