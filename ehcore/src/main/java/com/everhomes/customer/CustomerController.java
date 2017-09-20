package com.everhomes.customer;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.search.EnterpriseCustomerSearcher;
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

/**
 * Created by ying.xiong on 2017/8/1.
 */

@RestController
@RequestMapping("/customer")
public class CustomerController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EnterpriseCustomerSearcher enterpriseCustomerSearcher;
    /**
     * <b>URL: /customer/createCustomer</b>
     * <p>创建新客户</p>
     */
    @RequestMapping("createCustomer")
    @RestReturn(value = String.class)
    public RestResponse createCustomer(@Valid CreateCustomerCommand cmd) {
        if(CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(cmd.getCustomerType()))) {

        } else if(CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(cmd.getCustomerType()))) {
            propertyMgrService.createOrganizationOwner(cmd.getIndividualCustomer());
        }
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/createEnterpriseCustomer</b>
     * <p>创建企业客户</p>
     */
    @RequestMapping("createEnterpriseCustomer")
    @RestReturn(value = EnterpriseCustomerDTO.class)
    public RestResponse createEnterpriseCustomer(@Valid CreateEnterpriseCustomerCommand cmd) {
        RestResponse response = new RestResponse(customerService.createEnterpriseCustomer(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateEnterpriseCustomer</b>
     * <p>修改企业客户</p>
     */
    @RequestMapping("updateEnterpriseCustomer")
    @RestReturn(value = EnterpriseCustomerDTO.class)
    public RestResponse updateEnterpriseCustomer(@Valid UpdateEnterpriseCustomerCommand cmd) {
        RestResponse response = new RestResponse(customerService.updateEnterpriseCustomer(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteEnterpriseCustomer</b>
     * <p>删除</p>
     */
    @RequestMapping("deleteEnterpriseCustomer")
    @RestReturn(value = String.class)
    public RestResponse deleteEnterpriseCustomer(@Valid DeleteEnterpriseCustomerCommand cmd) {
        customerService.deleteEnterpriseCustomer(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getEnterpriseCustomer</b>
     * <p>查看企业客户信息</p>
     */
    @RequestMapping("getEnterpriseCustomer")
    @RestReturn(value = EnterpriseCustomerDTO.class)
    public RestResponse getEnterpriseCustomer(@Valid GetEnterpriseCustomerCommand cmd) {
        EnterpriseCustomerDTO dto = customerService.getEnterpriseCustomer(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/searchEnterpriseCustomer</b>
     * <p>列出企业客户</p>
     */
    @RequestMapping("searchEnterpriseCustomer")
    @RestReturn(value = SearchEnterpriseCustomerResponse.class)
    public RestResponse searchEnterpriseCustomer(@Valid SearchEnterpriseCustomerCommand cmd) {
        RestResponse response = new RestResponse(enterpriseCustomerSearcher.queryEnterpriseCustomers(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/syncEnterpriseCustomer</b>
     * <p>同步企业客户</p>
     */
    @RequestMapping("syncEnterpriseCustomer")
    @RestReturn(value = String.class)
    public RestResponse syncEnterpriseCustomer() {
        enterpriseCustomerSearcher.syncFromDb();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/importEnterpriseCustomerData</b>
     * <p>导入企业客户信息</p>
     */
    @RequestMapping("importEnterpriseCustomerData")
    @RestReturn(value = ImportFileTaskDTO.class)
    public RestResponse importEnterpriseCustomerData(@Valid ImportEnterpriseCustomerDataCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        User manaUser = UserContext.current().getUser();
        Long userId = manaUser.getId();
        if (null == files || null == files[0]) {
            LOGGER.error("files is null。userId=" + userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }

        RestResponse response = new RestResponse(customerService.importEnterpriseCustomer(cmd, files[0], userId));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    //税务信息
    /**
     * <b>URL: /customer/createCustomerTax</b>
     * <p>新建客户税务信息</p>
     */
    @RequestMapping("createCustomerTax")
    @RestReturn(value = String.class)
    public RestResponse createCustomerTax(@Valid CreateCustomerTaxCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerTax</b>
     * <p>修改企业客户税务信息</p>
     */
    @RequestMapping("updateCustomerTax")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerTax(@Valid UpdateCustomerTaxCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerTax</b>
     * <p>删除企业客户税务信息</p>
     */
    @RequestMapping("deleteCustomerTax")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerTax(@Valid DeleteCustomerTaxCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/findCustomerTax</b>
     * <p>查看企业客户税务信息</p>
     */
    @RequestMapping("findCustomerTax")
    @RestReturn(value = CustomerTaxDTO.class)
    public RestResponse findCustomerTax(@Valid GetCustomerTaxCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerTaxes</b>
     * <p>列出企业客户税务信息</p>
     */
    @RequestMapping("listCustomerTaxes")
    @RestReturn(value = CustomerTaxDTO.class, collection = true)
    public RestResponse listCustomerTaxes(@Valid ListCustomerTaxesCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    银行账号
    /**
     * <b>URL: /customer/createCustomerAccount</b>
     * <p>新建客户银行账号</p>
     */
    @RequestMapping("createCustomerAccount")
    @RestReturn(value = String.class)
    public RestResponse createCustomerAccount(@Valid CreateCustomerAccountCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerAccount</b>
     * <p>修改企业客户银行账号</p>
     */
    @RequestMapping("updateCustomerAccount")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerAccount(@Valid UpdateCustomerAccountCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerAccount</b>
     * <p>删除企业客户银行账号</p>
     */
    @RequestMapping("deleteCustomerAccount")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerAccount(@Valid DeleteCustomerAccountCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/findCustomerAccount</b>
     * <p>查看企业客户银行账号</p>
     */
    @RequestMapping("findCustomerAccount")
    @RestReturn(value = CustomerAccountDTO.class)
    public RestResponse findCustomerAccount(@Valid GetCustomerAccountCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerAccounts</b>
     * <p>列出企业客户银行账号</p>
     */
    @RequestMapping("listCustomerAccounts")
    @RestReturn(value = CustomerAccountDTO.class, collection = true)
    public RestResponse listCustomerAccounts(@Valid ListCustomerAccountsCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    //人才团队
    /**
     * <b>URL: /customer/createCustomerTalent</b>
     * <p>新建企业人才</p>
     */
    @RequestMapping("createCustomerTalent")
    @RestReturn(value = String.class)
    public RestResponse createCustomerTalent(@Valid CreateCustomerTalentCommand cmd) {
        customerService.createCustomerTalent(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerTalent</b>
     * <p>修改企业人才</p>
     */
    @RequestMapping("updateCustomerTalent")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerTalent(@Valid UpdateCustomerTalentCommand cmd) {
        customerService.updateCustomerTalent(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerTalent</b>
     * <p>删除企业人才</p>
     */
    @RequestMapping("deleteCustomerTalent")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerTalent(@Valid DeleteCustomerTalentCommand cmd) {
        customerService.deleteCustomerTalent(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerTalent</b>
     * <p>查看企业人才</p>
     */
    @RequestMapping("getCustomerTalent")
    @RestReturn(value = CustomerTalentDTO.class)
    public RestResponse getCustomerTalent(@Valid GetCustomerTalentCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerTalent(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerTalents</b>
     * <p>列出企业人才</p>
     */
    @RequestMapping("listCustomerTalents")
    @RestReturn(value = CustomerTalentDTO.class, collection = true)
    public RestResponse listCustomerTalents(@Valid ListCustomerTalentsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerTalents(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    知识产权
    /**
     * <b>URL: /customer/createCustomerTrademark</b>
     * <p>新建企业商标</p>
     */
    @RequestMapping("createCustomerTrademark")
    @RestReturn(value = String.class)
    public RestResponse createCustomerTrademark(@Valid CreateCustomerTrademarkCommand cmd) {
        customerService.createCustomerTrademark(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerTrademark</b>
     * <p>修改企业商标</p>
     */
    @RequestMapping("updateCustomerTrademark")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerTrademark(@Valid UpdateCustomerTrademarkCommand cmd) {
        customerService.updateCustomerTrademark(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerTrademark</b>
     * <p>删除企业商标</p>
     */
    @RequestMapping("deleteCustomerTrademark")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerTrademark(@Valid DeleteCustomerTrademarkCommand cmd) {
        customerService.deleteCustomerTrademark(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerTrademark</b>
     * <p>查看企业商标</p>
     */
    @RequestMapping("getCustomerTrademark")
    @RestReturn(value = CustomerTrademarkDTO.class)
    public RestResponse getCustomerTrademark(@Valid GetCustomerTrademarkCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerTrademark(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerTrademarks</b>
     * <p>列出企业商标</p>
     */
    @RequestMapping("listCustomerTrademarks")
    @RestReturn(value = CustomerTrademarkDTO.class, collection = true)
    public RestResponse listCustomerTrademarks(@Valid ListCustomerTrademarksCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerTrademarks(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/createCustomerPatent</b>
     * <p>新建企业专利</p>
     */
    @RequestMapping("createCustomerPatent")
    @RestReturn(value = String.class)
    public RestResponse createCustomerPatent(@Valid CreateCustomerPatentCommand cmd) {
        customerService.createCustomerPatent(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerPatent</b>
     * <p>修改企业专利</p>
     */
    @RequestMapping("updateCustomerPatent")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerPatent(@Valid UpdateCustomerPatentCommand cmd) {
        customerService.updateCustomerPatent(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerPatent</b>
     * <p>删除企业专利</p>
     */
    @RequestMapping("deleteCustomerPatent")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerPatent(@Valid DeleteCustomerPatentCommand cmd) {
        customerService.deleteCustomerPatent(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerPatent</b>
     * <p>查看企业专利</p>
     */
    @RequestMapping("getCustomerPatent")
    @RestReturn(value = CustomerPatentDTO.class)
    public RestResponse getCustomerPatent(@Valid GetCustomerPatentCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerPatent(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerPatents</b>
     * <p>列出企业专利</p>
     */
    @RequestMapping("listCustomerPatents")
    @RestReturn(value = CustomerPatentDTO.class, collection = true)
    public RestResponse listCustomerPatents(@Valid ListCustomerPatentsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerPatents(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/createCustomerCertificate</b>
     * <p>新建企业证书</p>
     */
    @RequestMapping("createCustomerCertificate")
    @RestReturn(value = String.class)
    public RestResponse createCustomerCertificate(@Valid CreateCustomerCertificateCommand cmd) {
        customerService.createCustomerCertificate(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerCertificate</b>
     * <p>修改企业证书</p>
     */
    @RequestMapping("updateCustomerCertificate")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerCertificate(@Valid UpdateCustomerCertificateCommand cmd) {
        customerService.updateCustomerCertificate(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerCertificate</b>
     * <p>删除企业证书</p>
     */
    @RequestMapping("deleteCustomerCertificate")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerCertificate(@Valid DeleteCustomerCertificateCommand cmd) {
        customerService.deleteCustomerCertificate(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerCertificate</b>
     * <p>查看企业专利</p>
     */
    @RequestMapping("getCustomerCertificate")
    @RestReturn(value = CustomerCertificateDTO.class)
    public RestResponse getCustomerCertificate(@Valid GetCustomerCertificateCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerCertificate(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerCertificates</b>
     * <p>列出企业专利</p>
     */
    @RequestMapping("listCustomerCertificates")
    @RestReturn(value = CustomerCertificateDTO.class, collection = true)
    public RestResponse listCustomerCertificates(@Valid ListCustomerCertificatesCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerCertificates(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    申报项目
    /**
     * <b>URL: /customer/createCustomerApplyProject</b>
     * <p>新建企业申报项目</p>
     */
    @RequestMapping("createCustomerApplyProject")
    @RestReturn(value = String.class)
    public RestResponse createCustomerApplyProject(@Valid CreateCustomerApplyProjectCommand cmd) {
        customerService.createCustomerApplyProject(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerApplyProject</b>
     * <p>修改企业申报项目</p>
     */
    @RequestMapping("updateCustomerApplyProject")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerApplyProject(@Valid UpdateCustomerApplyProjectCommand cmd) {
        customerService.updateCustomerApplyProject(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerApplyProject</b>
     * <p>删除企业申报项目</p>
     */
    @RequestMapping("deleteCustomerApplyProject")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerApplyProject(@Valid DeleteCustomerApplyProjectCommand cmd) {
        customerService.deleteCustomerApplyProject(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerApplyProject</b>
     * <p>查看企业申报项目</p>
     */
    @RequestMapping("getCustomerApplyProject")
    @RestReturn(value = CustomerApplyProjectDTO.class)
    public RestResponse getCustomerApplyProject(@Valid GetCustomerApplyProjectCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerApplyProject(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerApplyProjects</b>
     * <p>列出企业申报项目</p>
     */
    @RequestMapping("listCustomerApplyProjects")
    @RestReturn(value = CustomerApplyProjectDTO.class, collection = true)
    public RestResponse listCustomerApplyProjects(@Valid ListCustomerApplyProjectsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerApplyProjects(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    //工商信息
    /**
     * <b>URL: /customer/createCustomerCommercial</b>
     * <p>新建企业工商信息</p>
     */
    @RequestMapping("createCustomerCommercial")
    @RestReturn(value = String.class)
    public RestResponse createCustomerCommercial(@Valid CreateCustomerCommercialCommand cmd) {
        customerService.createCustomerCommercial(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerCommercial</b>
     * <p>修改企业工商信息</p>
     */
    @RequestMapping("updateCustomerCommercial")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerCommercial(@Valid UpdateCustomerCommercialCommand cmd) {
        customerService.updateCustomerCommercial(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerCommercial</b>
     * <p>删除企业工商信息</p>
     */
    @RequestMapping("deleteCustomerCommercial")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerCommercial(@Valid DeleteCustomerCommercialCommand cmd) {
        customerService.deleteCustomerCommercial(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerCommercial</b>
     * <p>查看企业工商信息</p>
     */
    @RequestMapping("getCustomerCommercial")
    @RestReturn(value = CustomerCommercialDTO.class)
    public RestResponse getCustomerCommercial(@Valid GetCustomerCommercialCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerCommercial(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerCommercials</b>
     * <p>列出企业工商信息</p>
     */
    @RequestMapping("listCustomerCommercials")
    @RestReturn(value = CustomerCommercialDTO.class, collection = true)
    public RestResponse listCustomerCommercials(@Valid ListCustomerCommercialsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerCommercials(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    投融情况
    /**
     * <b>URL: /customer/createCustomerInvestment</b>
     * <p>新建企业投融情况</p>
     */
    @RequestMapping("createCustomerInvestment")
    @RestReturn(value = String.class)
    public RestResponse createCustomerInvestment(@Valid CreateCustomerInvestmentCommand cmd) {
        customerService.createCustomerInvestment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerInvestment</b>
     * <p>修改企业投融情况</p>
     */
    @RequestMapping("updateCustomerInvestment")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerInvestment(@Valid UpdateCustomerInvestmentCommand cmd) {
        customerService.updateCustomerInvestment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerInvestment</b>
     * <p>删除企业投融情况</p>
     */
    @RequestMapping("deleteCustomerInvestment")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerInvestment(@Valid DeleteCustomerInvestmentCommand cmd) {
        customerService.deleteCustomerInvestment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerInvestment</b>
     * <p>查看企业投融情况</p>
     */
    @RequestMapping("getCustomerInvestment")
    @RestReturn(value = CustomerInvestmentDTO.class)
    public RestResponse getCustomerInvestment(@Valid GetCustomerInvestmentCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerInvestment(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerInvestments</b>
     * <p>列出企业投融情况</p>
     */
    @RequestMapping("listCustomerInvestments")
    @RestReturn(value = CustomerInvestmentDTO.class, collection = true)
    public RestResponse listCustomerInvestments(@Valid ListCustomerInvestmentsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerInvestments(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    经济指标 的增删改查
    /**
     * <b>URL: /customer/createCustomerEconomicIndicator</b>
     * <p>新建企业经济指标</p>
     */
    @RequestMapping("createCustomerEconomicIndicator")
    @RestReturn(value = String.class)
    public RestResponse createCustomerEconomicIndicator(@Valid CreateCustomerEconomicIndicatorCommand cmd) {
        customerService.createCustomerEconomicIndicator(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerEconomicIndicator</b>
     * <p>修改企业经济指标</p>
     */
    @RequestMapping("updateCustomerEconomicIndicator")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerEconomicIndicator(@Valid UpdateCustomerEconomicIndicatorCommand cmd) {
        customerService.updateCustomerEconomicIndicator(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerEconomicIndicator</b>
     * <p>删除企业经济指标</p>
     */
    @RequestMapping("deleteCustomerEconomicIndicator")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerEconomicIndicator(@Valid DeleteCustomerEconomicIndicatorCommand cmd) {
        customerService.deleteCustomerEconomicIndicator(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerEconomicIndicator</b>
     * <p>查看企业经济指标</p>
     */
    @RequestMapping("getCustomerEconomicIndicator")
    @RestReturn(value = CustomerEconomicIndicatorDTO.class)
    public RestResponse getCustomerEconomicIndicator(@Valid GetCustomerEconomicIndicatorCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerEconomicIndicator(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerEconomicIndicators</b>
     * <p>列出企业经济指标</p>
     */
    @RequestMapping("listCustomerEconomicIndicators")
    @RestReturn(value = CustomerEconomicIndicatorDTO.class, collection = true)
    public RestResponse listCustomerEconomicIndicators(@Valid ListCustomerEconomicIndicatorsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerEconomicIndicators(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listEnterpriseCustomerStatistics</b>
     * <p>列出企业客户统计信息</p>
     */
    @RequestMapping("listEnterpriseCustomerStatistics")
    @RestReturn(value = EnterpriseCustomerStatisticsDTO.class)
    public RestResponse listEnterpriseCustomerStatistics(@Valid ListEnterpriseCustomerStatisticsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listEnterpriseCustomerStatistics(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerIndustryStatistics</b>
     * <p>列出企业行业分布统计信息</p>
     */
    @RequestMapping("listCustomerIndustryStatistics")
    @RestReturn(value = CustomerIndustryStatisticsResponse.class)
    public RestResponse listCustomerIndustryStatistics(@Valid ListEnterpriseCustomerStatisticsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerIndustryStatistics(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerIntellectualPropertyStatistics</b>
     * <p>列出企业知识产权分布统计信息</p>
     */
    @RequestMapping("listCustomerIntellectualPropertyStatistics")
    @RestReturn(value = CustomerIntellectualPropertyStatisticsResponse.class)
    public RestResponse listCustomerIntellectualPropertyStatistics(@Valid ListEnterpriseCustomerStatisticsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerIntellectualPropertyStatistics(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerTalentStatistics</b>
     * <p>列出企业人才分布统计信息</p>
     */
    @RequestMapping("listCustomerTalentStatistics")
    @RestReturn(value = CustomerTalentStatisticsResponse.class)
    public RestResponse listCustomerTalentStatistics(@Valid ListEnterpriseCustomerStatisticsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerTalentStatistics(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerProjectStatistics</b>
     * <p>列出企业获批项目分布统计信息</p>
     */
    @RequestMapping("listCustomerProjectStatistics")
    @RestReturn(value = CustomerProjectStatisticsResponse.class)
    public RestResponse listCustomerProjectStatistics(@Valid ListEnterpriseCustomerStatisticsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerProjectStatistics(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerSourceStatistics</b>
     * <p>列出企业认知途径分布统计信息</p>
     */
    @RequestMapping("listCustomerSourceStatistics")
    @RestReturn(value = CustomerSourceStatisticsResponse.class)
    public RestResponse listCustomerSourceStatistics(@Valid ListEnterpriseCustomerStatisticsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerSourceStatistics(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/syncEnterpriseCustomers</b>
     * <p>同步企业客户</p>
     */
    @RequestMapping("syncEnterpriseCustomers")
    @RestReturn(value = String.class)
    public RestResponse syncEnterpriseCustomers(@Valid SyncCustomersCommand cmd) {
        customerService.syncEnterpriseCustomers(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/syncIndividualCustomers</b>
     * <p>同步个人客户</p>
     */
    @RequestMapping("syncIndividualCustomers")
    @RestReturn(value = String.class)
    public RestResponse syncIndividualCustomers(@Valid SyncCustomersCommand cmd) {
        customerService.syncIndividualCustomers(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/listCustomerTrackings</b>
     * <p>列出跟进信息</p>
     */
    @RequestMapping("listCustomerTrackings")
    @RestReturn(value = CustomerTrackingDTO.class, collection = true)
    public RestResponse listCustomerTrackings(@Valid ListCustomerTrackingsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerTrackings(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
    /**
     * <b>URL: /customer/getCustomerTracking</b>
     * <p>查看跟进信息</p>
     */
    @RequestMapping("getCustomerTracking")
    @RestReturn(value = CustomerTrackingDTO.class)
    public RestResponse getCustomerTracking(@Valid GetCustomerTrackingCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerTracking(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/updateCustomerTracking</b>
     * <p>修改跟进信息</p>
     */
    @RequestMapping("updateCustomerTracking")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerTracking(@Valid UpdateCustomerTrackingCommand cmd) {
        customerService.updateCustomerTracking(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/deleteCustomerTracking</b>
     * <p>删除跟进信息</p>
     */
    @RequestMapping("deleteCustomerTracking")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerTracking(@Valid DeleteCustomerTrackingCommand cmd) {
        customerService.deleteCustomerTracking(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/createCustomerTracking</b>
     * <p>新建跟进信息</p>
     */
    @RequestMapping("createCustomerTracking")
    @RestReturn(value = String.class)
    public RestResponse createCustomerTracking(@Valid CreateCustomerTrackingCommand cmd) {
        customerService.createCustomerTracking(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
