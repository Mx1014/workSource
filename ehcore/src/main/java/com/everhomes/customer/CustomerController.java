package com.everhomes.customer;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * <b>URL: /customer/createEnterpriseCustomer</b>
     * <p>创建企业客户</p>
     */
    @RequestMapping("createEnterpriseCustomer")
    @RestReturn(value = String.class)
    public RestResponse createEnterpriseCustomer(@Valid CreateEnterpriseCustomerCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /customer/updateEnterpriseCustomer</b>
     * <p>修改企业客户</p>
     */
    @RequestMapping("updateEnterpriseCustomer")
    @RestReturn(value = String.class)
    public RestResponse updateEnterpriseCustomer(@Valid UpdateEnterpriseCustomerCommand cmd) {
        RestResponse response = new RestResponse();
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
        RestResponse response = new RestResponse();
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
//        enterpriseCustomerService.importEnterpriseData(cmd, files[0], userId);
        RestResponse response = new RestResponse();
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
    public RestResponse findCustomerTax(@Valid FindCustomerTaxCommand cmd) {
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
    public RestResponse findCustomerAccount(@Valid FindCustomerAccountCommand cmd) {
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

    //人才团队 知识产权 申报项目 工商信息 投融情况 经济指标 的增删改查

    /**
     * <b>URL: /customer/listEnterpriseCustomerStatistics</b>
     * <p>列出企业客户统计信息</p>
     */
    @RequestMapping("listEnterpriseCustomerStatistics")
    @RestReturn(value = EnterpriseCustomerStatisticsDTO.class)
    public RestResponse listEnterpriseCustomerStatistics(@Valid ListEnterpriseCustomerStatisticsCommand cmd) {
        RestResponse response = new RestResponse();
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
        RestResponse response = new RestResponse();
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
        RestResponse response = new RestResponse();
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
        RestResponse response = new RestResponse();
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
        RestResponse response = new RestResponse();
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
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
