package com.everhomes.customer.openapi;

import com.everhomes.community.CommunityService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListBuildingCommandResponse;
import com.everhomes.rest.community.ListCommunitiesByCategoryCommand;
import com.everhomes.rest.community.ListCommunitiesByKeywordCommandResponse;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.openapi.DeleteEnterpriseCommand;
import com.everhomes.rest.customer.openapi.OpenApiUpdateCustomerCommand;
import com.everhomes.search.EnterpriseCustomerSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openapi")
public class CustomerOpenApiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOpenApiController.class);

    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private OpenApiCustomerService customerService;

    @Autowired
    private EnterpriseCustomerSearcher enterpriseCustomerSearcher;

    @Autowired
    private CommunityService communityService;

    /**
     * <b>URL: /asset/listCommunities</b>
     * <p>查看项目列表</p>
     */
    @RequestMapping("listCommunities")
    @RestReturn(value = String.class)
    public RestResponse listCommunities(ListCommunitiesByCategoryCommand cmd) {
        ListCommunitiesByKeywordCommandResponse resp = communityService.listCommunitiesByCategory(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/listBuildings</b>
     * <p>查看楼栋列表</p>
     */
    @RequestMapping("listBuildings")
    @RestReturn(value = String.class)
    public RestResponse listBuildings(ListBuildingCommand cmd) {
        ListBuildingCommandResponse buildings = communityService.listBuildings(cmd);
        RestResponse response = new RestResponse(buildings);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/createEnterprise</b>
     * <p>创建企业</p>
     */
    @RequestMapping("createEnterprise")
    @RestReturn(value = String.class)
    public RestResponse createEnterprise(OpenApiUpdateCustomerCommand cmd) {
        EnterpriseCustomerDTO enterpriseCustomerDTO = customerService.createEnterpriseCustomer(cmd);
        RestResponse response = new RestResponse(enterpriseCustomerDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/updateEnterprise</b>
     * <p>创建企业</p>
     */
    @RequestMapping("updateEnterprise")
    @RestReturn(value = String.class)
    public RestResponse updateEnterprise(OpenApiUpdateCustomerCommand cmd) {
        EnterpriseCustomerDTO enterpriseCustomerDTO = customerService.createEnterpriseCustomer(cmd);
        RestResponse response = new RestResponse(enterpriseCustomerDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/deleteEnterprise</b>
     * <p>创建企业</p>
     */
    @RequestMapping("deleteEnterprise")
    @RestReturn(value = String.class)
    public RestResponse deleteEnterprise(DeleteEnterpriseCommand cmd) {
        EnterpriseCustomerDTO enterpriseCustomerDTO = customerService.deleteEnterpriseCustomer(cmd);
        RestResponse response = new RestResponse(enterpriseCustomerDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/createEnterprisAdmin</b>
     * <p>创建企业管理员</p>
     */
    @RequestMapping("createEnterprisAdmin")
    @RestReturn(value = String.class)
    public RestResponse createEnterprisAdmin(DeleteEnterpriseCommand cmd) {
        customerService.createEnterpriseAdmin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/deleteEnterprisAdmin</b>
     * <p>删除管理员</p>
     */
    @RequestMapping("deleteEnterprisAdmin")
    @RestReturn(value = String.class)
    public RestResponse deleteEnterprisAdmin(DeleteEnterpriseCommand cmd) {
        customerService.deleteEnterpriseAdmin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
