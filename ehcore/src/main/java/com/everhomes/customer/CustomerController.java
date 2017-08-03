package com.everhomes.customer;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.enterprise_customer.*;
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
}
