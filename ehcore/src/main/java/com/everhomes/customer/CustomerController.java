package com.everhomes.customer;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.DeleteOrganizationAdminCommand;
import com.everhomes.rest.activity.ListSignupInfoByOrganizationIdResponse;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.energy.ListCommnutyRelatedMembersCommand;
import com.everhomes.rest.enterprise.DeleteEnterpriseCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.rentalv2.ListRentalBillsCommandResponse;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

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
        customerService.deleteEnterpriseCustomer(cmd, true);
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
     * <p>列出企业客户(有权限 包括个人权限和权限细化)</p>
     */
    @RequestMapping("searchEnterpriseCustomer")
    @RestReturn(value = SearchEnterpriseCustomerResponse.class)
    public RestResponse searchEnterpriseCustomer(@Valid SearchEnterpriseCustomerCommand cmd) {
        RestResponse response = new RestResponse(customerService.queryEnterpriseCustomers(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: </b>
     * <p>导出企业客户excel</p>
     */
    @RequestMapping("exportEnterpriseCustomerss")
    public void exportEnterpriseCustomer(@Valid ExportEnterpriseCustomerCommand cmd, HttpServletResponse response) {
        customerService.exportEnterpriseCustomer(cmd, response);
    }


    /**
     * <b>URL: /customer/exportEnterpriseCustomer</b>
     * <p>导出企业客户excel</p>
     */
    @RequestMapping("exportEnterpriseCustomer")
    @RestReturn(value = String.class)
    public RestResponse exportEnterpriseCustomer(@Valid ExportEnterpriseCustomerCommand cmd) {
        customerService.exportContractListByContractList(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }



    /**
     * <b>URL: /customer/exportEnterpriseCustomerTemplate</b>
     * <p>导出企业客户excel模板</p>
     */
    @RequestMapping("exportEnterpriseCustomerTemplate")
    public void exportEnterpriseCustomerTemplate(ListFieldGroupCommand cmd, HttpServletResponse response) {
        customerService.exportEnterpriseCustomerTemplate(cmd, response);
    }

    /**
     * <b>URL: /customer/searchEnterpriseCustomerWithoutAuth</b>
     * <p>列出企业客户（不进行权限校验）</p>
     */
    @RequestMapping("searchEnterpriseCustomerWithoutAuth")
    @RestReturn(value = SearchEnterpriseCustomerResponse.class)
    public RestResponse searchEnterpriseCustomerWithoutAuth(@Valid SearchEnterpriseCustomerCommand cmd) {
        RestResponse response = new RestResponse(enterpriseCustomerSearcher.queryEnterpriseCustomers(cmd,true));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
     /**
     * <b>URL: /customer/easySearchEnterpriseCustomers</b>
     * <p>快速检出列出企业客户（不进行权限校验）</p>
     */
    @RequestMapping("easySearchEnterpriseCustomers")
    @RestReturn(value = EasySearchEnterpriseCustomersDTO.class, collection = true)
    public RestResponse easySearchEnterpriseCustomers(EasySearchEnterpriseCustomersCommand cmd) {
        List<EasySearchEnterpriseCustomersDTO> dtos = enterpriseCustomerSearcher.easyQueryEnterpriseCustomers(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /customer/listEnterpriseCustomers</b>
     * <p>列出所有企业客户</p>
     */
    @RequestMapping("listEnterpriseCustomers")
    @RestReturn(value = EasySearchEnterpriseCustomersDTO.class, collection = true)
    public RestResponse listEnterpriseCustomer(EasySearchEnterpriseCustomersCommand cmd) {
        List<EasySearchEnterpriseCustomersDTO> dtos = enterpriseCustomerSearcher.listEnterpriseCustomers(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/syncEnterpriseCustomerIndex</b>
     * <p>同步企业客户</p>
     */
    @RequestMapping("syncEnterpriseCustomerIndex")
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
        customerService.createCustomerTax(cmd);
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
        customerService.updateCustomerTax(cmd);
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
        customerService.deleteCustomerTax(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerTax</b>
     * <p>查看企业客户税务信息</p>
     */
    @RequestMapping("getCustomerTax")
    @RestReturn(value = CustomerTaxDTO.class)
    public RestResponse getCustomerTax(@Valid GetCustomerTaxCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerTax(cmd));
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
        RestResponse response = new RestResponse(customerService.listCustomerTaxes(cmd));
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
        customerService.createCustomerAccount(cmd);
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
        customerService.updateCustomerAccount(cmd);
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
        customerService.deleteCustomerAccount(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerAccount</b>
     * <p>查看企业客户银行账号</p>
     */
    @RequestMapping("getCustomerAccount")
    @RestReturn(value = CustomerAccountDTO.class)
    public RestResponse getCustomerAccount(@Valid GetCustomerAccountCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerAccount(cmd));
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
        RestResponse response = new RestResponse(customerService.listCustomerAccounts(cmd));
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
     * <b>URL: /customer/listCustomerAnnualStatistics</b>
     * <p>列出企业年度营业额纳税额信息</p>
     */
    @RequestMapping("listCustomerAnnualStatistics")
    @RestReturn(value = ListCustomerAnnualStatisticsResponse.class)
    public RestResponse listCustomerAnnualStatistics(ListCustomerAnnualStatisticsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerAnnualStatistics(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerAnnualDetails</b>
     * <p>列出企业年度营业额纳税额信息</p>
     */
    @RequestMapping("listCustomerAnnualDetails")
    @RestReturn(value = ListCustomerAnnualDetailsResponse.class)
    public RestResponse listCustomerAnnualDetails(@Valid ListCustomerAnnualDetailsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerAnnualDetails(cmd));
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
        RestResponse response = new RestResponse(customerService.syncEnterpriseCustomers(cmd, false));
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
    @RestReturn(value = CustomerTrackingDTO.class)
    public RestResponse updateCustomerTracking(@Valid UpdateCustomerTrackingCommand cmd) {
        RestResponse response = new RestResponse(customerService.updateCustomerTracking(cmd));
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
    
    
    /**
     * <b>URL: /customer/listCustomerTrackingPlans</b>
     * <p>列出计划信息</p>
     */
    @RequestMapping("listCustomerTrackingPlans")
    @RestReturn(value = CustomerTrackingPlanDTO.class, collection = true)
    public RestResponse listCustomerTrackingPlans(@Valid ListCustomerTrackingPlansCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerTrackingPlans(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
    /**
     * <b>URL: /customer/getCustomerTrackingPlan</b>
     * <p>查看计划信息</p>
     */
    @RequestMapping("getCustomerTrackingPlan")
    @RestReturn(value = CustomerTrackingPlanDTO.class)
    public RestResponse getCustomerTrackingPlan(@Valid GetCustomerTrackingPlanCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerTrackingPlan(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/updateCustomerTrackingPlan</b>
     * <p>修改计划信息</p>
     */
    @RequestMapping("updateCustomerTrackingPlan")
    @RestReturn(value = CustomerTrackingPlanDTO.class)
    public RestResponse updateCustomerTrackingPlan(@Valid UpdateCustomerTrackingPlanCommand cmd) {
        RestResponse response = new RestResponse(customerService.updateCustomerTrackingPlan(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/deleteCustomerTrackingPlan</b>
     * <p>删除计划信息</p>
     */
    @RequestMapping("deleteCustomerTrackingPlan")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerTrackingPlan(@Valid DeleteCustomerTrackingPlanCommand cmd) {
        customerService.deleteCustomerTrackingPlan(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/createCustomerTrackingPlan</b>
     * <p>新建计划信息</p>
     */
    @RequestMapping("createCustomerTrackingPlan")
    @RestReturn(value = String.class)
    public RestResponse createCustomerTrackingPlan(@Valid CreateCustomerTrackingPlanCommand cmd) {
        customerService.createCustomerTrackingPlan(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/listCustomerEvents</b>
     * <p>列出客户事件</p>
     */
    @RequestMapping("listCustomerEvents")
    @RestReturn(value = CustomerEventDTO.class, collection = true)
    public RestResponse listCustomerEvents(@Valid ListCustomerEventsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerEvents(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/allotEnterpriseCustomer</b>
     * <p>分配客户</p>
     */
    @RequestMapping("allotEnterpriseCustomer")
    @RestReturn(value = String.class)
    public RestResponse allotEnterpriseCustomer(@Valid AllotEnterpriseCustomerCommand cmd) {
    	customerService.allotEnterpriseCustomer(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/giveUpEnterpriseCustomer</b>
     * <p>放弃客户</p>
     */
    @RequestMapping("giveUpEnterpriseCustomer")
    @RestReturn(value = String.class)
    public RestResponse giveUpEnterpriseCustomer(@Valid GiveUpEnterpriseCustomerCommand cmd) {
    	customerService.giveUpEnterpriseCustomer(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/listNearbyEnterpriseCustomers</b>
     * <p>获取附近客户列表</p>
     */
    @RequestMapping("listNearbyEnterpriseCustomers")
    @RestReturn(value=ListNearbyEnterpriseCustomersCommandResponse.class)
    public RestResponse listNearbyEnterpriseCustomers(@Valid ListNearbyEnterpriseCustomersCommand cmd) {
 		
    	ListNearbyEnterpriseCustomersCommandResponse res = customerService.listNearbyEnterpriseCustomers(cmd);

 		RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /customer/listCustomerTrackingPlansByDate</b>
     * <p>列出跟进计划--给APP</p>
     */
    @RequestMapping("listCustomerTrackingPlansByDate")
    @RestReturn(value = List.class, collection = true)
    public RestResponse listCustomerTrackingPlansByDate(@Valid ListCustomerTrackingPlansByDateCommand cmd) {
 		
 		RestResponse response = new RestResponse(customerService.listCustomerTrackingPlansByDate(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    //入驻信息
    /**
     * <b>URL: /customer/createCustomerEntryInfo</b>
     * <p>新建入驻信息</p>
     */
    @RequestMapping("createCustomerEntryInfo")
    @RestReturn(value = String.class)
    public RestResponse createCustomerEntryInfo(@Valid CreateCustomerEntryInfoCommand cmd) {
        customerService.createCustomerEntryInfo(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerEntryInfo</b>
     * <p>修改入驻信息</p>
     */
    @RequestMapping("updateCustomerEntryInfo")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerEntryInfo(@Valid UpdateCustomerEntryInfoCommand cmd) {
        customerService.updateCustomerEntryInfo(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerEntryInfo</b>
     * <p>删除入驻信息</p>
     */
    @RequestMapping("deleteCustomerEntryInfo")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerEntryInfo(@Valid DeleteCustomerEntryInfoCommand cmd) {
        customerService.deleteCustomerEntryInfo(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerEntryInfo</b>
     * <p>查看入驻信息</p>
     */
    @RequestMapping("getCustomerEntryInfo")
    @RestReturn(value = CustomerEntryInfoDTO.class)
    public RestResponse getCustomerEntryInfo(@Valid GetCustomerEntryInfoCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerEntryInfo(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerEntryInfos</b>
     * <p>列出入驻信息</p>
     */
    @RequestMapping("listCustomerEntryInfos")
    @RestReturn(value = CustomerEntryInfoDTO.class, collection = true)
    public RestResponse listCustomerEntryInfos(@Valid ListCustomerEntryInfosCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerEntryInfos(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerEntryInfosWithoutAuth</b>
     * <p>列出入驻信息(无权限)</p>
     */
    @RequestMapping("listCustomerEntryInfosWithoutAuth")
    @RestReturn(value = CustomerEntryInfoDTO.class, collection = true)
    public RestResponse listCustomerEntryInfosWithoutAuth(@Valid ListCustomerEntryInfosCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerEntryInfosWithoutAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    //离场信息
    /**
     * <b>URL: /customer/createCustomerDepartureInfo</b>
     * <p>新建离场信息</p>
     */
    @RequestMapping("createCustomerDepartureInfo")
    @RestReturn(value = String.class)
    public RestResponse createCustomerDepartureInfo(@Valid CreateCustomerDepartureInfoCommand cmd) {
        customerService.createCustomerDepartureInfo(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateCustomerDepartureInfo</b>
     * <p>修改离场信息</p>
     */
    @RequestMapping("updateCustomerDepartureInfo")
    @RestReturn(value = String.class)
    public RestResponse updateCustomerDepartureInfo(@Valid UpdateCustomerDepartureInfoCommand cmd) {
        customerService.updateCustomerDepartureInfo(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteCustomerDepartureInfo</b>
     * <p>删除离场信息</p>
     */
    @RequestMapping("deleteCustomerDepartureInfo")
    @RestReturn(value = String.class)
    public RestResponse deleteCustomerDepartureInfo(@Valid DeleteCustomerDepartureInfoCommand cmd) {
        customerService.deleteCustomerDepartureInfo(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/getCustomerDepartureInfo</b>
     * <p>查看离场信息</p>
     */
    @RequestMapping("getCustomerDepartureInfo")
    @RestReturn(value = CustomerDepartureInfoDTO.class)
    public RestResponse getCustomerDepartureInfo(@Valid GetCustomerDepartureInfoCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerDepartureInfo(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerDepartureInfos</b>
     * <p>列出离场信息</p>
     */
    @RequestMapping("listCustomerDepartureInfos")
    @RestReturn(value = CustomerDepartureInfoDTO.class, collection = true)
    public RestResponse listCustomerDepartureInfos(@Valid ListCustomerDepartureInfosCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerDepartureInfos(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    //资源预定和服务联盟
    /**
     * <b>URL: /customer/listCustomerSeviceAllianceAppRecords</b>
     * <p>列出服务联盟信息</p>
     */
    @RequestMapping("listCustomerSeviceAllianceAppRecords")
    @RestReturn(value = SearchRequestInfoResponse.class)
    public RestResponse listCustomerSeviceAllianceAppRecords (@Valid ListCustomerSeviceAllianceAppRecordsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerSeviceAllianceAppRecords(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerRentalBills</b>
     * <p>列出资源预定信息</p>
     */
    @RequestMapping("listCustomerRentalBills")
    @RestReturn(value = ListRentalBillsCommandResponse.class)
    public RestResponse listCustomerRentalBills (@Valid ListCustomerRentalBillsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCustomerRentalBills(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCommunitySyncResult</b>
     * <p>列出同步信息</p>
     */
    @RequestMapping("listCommunitySyncResult")
    @RestReturn(value = ListCommunitySyncResultResponse.class)
    public RestResponse listCommunitySyncResult(ListCommunitySyncResultCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCommunitySyncResult(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCommunityUserRelatedTrackUsers</b>
     * <p>列出项目下相关的跟进人（只有管理员才有）</p>
     */
    @RequestMapping("listCommunityUserRelatedTrackUsers")
    @RestReturn(value = OrganizationMemberDTO.class,collection = true)
    public RestResponse listCommunityUserRelatedTrackUsers(ListCommunitySyncResultCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCommunityUserRelatedTrackUsers(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCommunityRelatedMembers</b>
     * <p>列出项目下相关的所有人(包含超级管理员和应用管理员)</p>
     */
    @RequestMapping("listCommunityRelatedMembers")
    @RestReturn(value = OrganizationMemberDTO.class,collection = true)
    public RestResponse listCommunityUserRelatedTrackUsers(ListCommnutyRelatedMembersCommand cmd) {
        RestResponse response = new RestResponse(customerService.listCommunityRelatedMembers(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/checkCustomerCurrentUserAdmin</b>
     * <p>校验管理员(app拿不到appId 暂时不改公共接口)</p>
     */
    @RequestMapping("checkCustomerCurrentUserAdmin")
    @RestReturn(value = Byte.class)
    public RestResponse checkCustomerCurrentUserAdmin(ListCommnutyRelatedMembersCommand cmd) {
        RestResponse response = new RestResponse(customerService.checkCustomerCurrentUserAdmin(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/syncResultViewed</b>
     * <p>是否看过同步结果</p>
     */
    @RequestMapping("syncResultViewed")
    @RestReturn(value = String.class)
    public RestResponse syncResultViewed(SyncResultViewedCommand cmd) {
        RestResponse response = new RestResponse(customerService.syncResultViewed(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/createOrganizationAdmin</b>
     * <p>创建Enterprise customer管理员</p>
     */
    @RequestMapping("createOrganizationAdmin")
    @RestReturn(value = String.class)
    public RestResponse createOrganizationAdmin(CreateOrganizationAdminCommand cmd) {
        customerService.createOrganizationAdmin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deleteOrganizationAdmin</b>
     * <p>删除Enterprise customer管理员</p>
     */
    @RequestMapping("deleteOrganizationAdmin")
    @RestReturn(value = String.class)
    public RestResponse deleteOrganizationAdmin(DeleteOrganizationAdminCommand cmd) {
        customerService.deleteOrganizationAdmin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listOrganizationAdmin</b>
     * <p>列出Enterprise customer管理员</p>
     */
    @RequestMapping("listOrganizationAdmin")
    @RestReturn(value = OrganizationContactDTO.class, collection = true)
    public RestResponse listOrganizationAdmin(ListServiceModuleAdministratorsCommand cmd) {
        RestResponse response = new RestResponse(customerService.listOrganizationAdmin(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /customer/syncOrganizationToCustomer</b>
     * <p>迁移企业管理数据到企业客户管理</p>
     */
    @RequestMapping("syncOrganizationToCustomer")
    @RestReturn(value = String.class)
    public RestResponse syncOrganizationToCustomer(SyncCustomerDataCommand cmd) {
        customerService.syncOrganizationToCustomer(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/exportCustomerDetails</b>
     * <p>导出客户统计信息</p>
     */
    @RequestMapping("exportCustomerDetails")
    public HttpServletResponse exportCustomerDetails(ListEnterpriseCustomerStatisticsCommand cmd, HttpServletResponse httpResponse) {
        return customerService.exportCustomerDetails(cmd,httpResponse);
    }

    /**
     * <b>URL: /customer/getCustomerBasicInfoByOrgId</b>
     * <p>根据企业id获取客户具体基础信息</p>
     */
    @RequestMapping("getCustomerBasicInfoByOrgId")
    public RestResponse getCustomerBasicInfoByOrgId(GetEnterpriseCustomerCommand cmd) {
        RestResponse response = new RestResponse(customerService.getCustomerBasicInfoByOrgId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/deletePotentialCustomer</b>
     * <p>删除潜在客户信息</p>
     */
    @RequestMapping("deletePotentialCustomer")
    @RestReturn(value = String.class)
    public RestResponse deletePotentialCustomer(DeleteEnterpriseCommand cmd) {
        customerService.deletePotentialCustomer(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updatePotentialCustomer</b>
     * <p>修改潜在客户信息</p>
     */
    @RequestMapping("updatePotentialCustomer")
    @RestReturn(value = String.class)
    public RestResponse updatePotentialCustomer(DeleteEnterpriseCommand cmd) {
        customerService.updatePotentialCustomer(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listPotentialCustomers</b>
     * <p>列表潜在客户信息</p>
     */
    @RequestMapping("listPotentialCustomers")
    @RestReturn(value = CustomerPotentialResponse.class)
    public RestResponse listPotentialCustomers(DeleteEnterpriseCommand cmd) {
        RestResponse response = new RestResponse(customerService.listPotentialCustomers(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/setSyncPotentialCustomer</b>
     * <p>设置客户同步</p>
     */
    @RequestMapping("setSyncPotentialCustomer")
    @RestReturn(value = String.class)
    public RestResponse setSyncPotentialCustomer(CustomerConfigurationCommand cmd) {
        customerService.setSyncPotentialCustomer(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listSyncPotentialCustomer</b>
     * <p>获取客户同步设置</p>
     */
    @RequestMapping("listSyncPotentialCustomer")
    @RestReturn(value = CustomerConfigurationDTO.class,collection = true)
    public RestResponse listSyncPotentialCustomer(CustomerConfigurationCommand cmd) {
       List<CustomerConfigurationDTO> customerConfigurations =  customerService.listSyncPotentialCustomer(cmd);
        RestResponse response = new RestResponse(customerConfigurations);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listExpandItems</b>
     * <p>获取服务联盟和活动多入口列表信息</p>
     */
    @RequestMapping("listExpandItems")
    @RestReturn(value = CustomerExpandItemDTO.class,collection = true)
    public RestResponse listExpandItems(CustomerConfigurationCommand cmd) {
       List<CustomerExpandItemDTO> items =  customerService.listExpandItems(cmd);
        RestResponse response = new RestResponse(items);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listPotentialTalent</b>
     * <p>获取潜在客户的人才团队</p>
     */
    @RequestMapping("listPotentialTalent")
    @RestReturn(value = CustomerTalentDTO.class,collection = true)
    public RestResponse listPotentialTalent(DeleteEnterpriseCommand cmd) {
       List<CustomerTalentDTO> dtos =  customerService.listPotentialTalent(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/changeCustomerAptitude</b>
     * <p>一键转为资质客户</p>
     */
    @RequestMapping("changeCustomerAptitude")
    @RestReturn(value = String.class)
    public RestResponse changeCustomerAptitude(@Valid SearchEnterpriseCustomerCommand cmd) {
        customerService.changeCustomerAptitude(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/listCustomerApartmentActivity</b>
     * <p>列出租客活动信息</p>
     */
    @RequestMapping("listCustomerApartmentActivity")
    @RestReturn(value = ListSignupInfoByOrganizationIdResponse.class)
    public RestResponse listCustomerApartmentActivity(@Valid ListCustomerApartmentActivityCommand cmd) {
        ListSignupInfoByOrganizationIdResponse dto = customerService.listCustomerApartmentActivity(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
