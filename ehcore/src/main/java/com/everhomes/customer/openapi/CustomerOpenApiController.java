package com.everhomes.customer.openapi;

import com.everhomes.community.CommunityService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.customer.CustomerController;
import com.everhomes.customer.CustomerService;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListBuildingCommandResponse;
import com.everhomes.rest.community.ListCommunitiesByCategoryCommand;
import com.everhomes.rest.community.ListCommunitiesByKeywordCommandResponse;
import com.everhomes.search.EnterpriseCustomerSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Rui.Jia  2018/7/10 10 :08
 */
@RestController
@RequestMapping("/openapi")
public class CustomerOpenApiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private CustomerService customerService;

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
    public RestResponse listCommunities( ListCommunitiesByCategoryCommand cmd) {
        ListCommunitiesByKeywordCommandResponse resp = communityService.listCommunitiesByCategory(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: i/asset/listBuildings</b>
     * <p>查看楼栋列表</p>
     */
    @RequestMapping("listBuildings")
    @RestReturn(value = String.class)
    public RestResponse listBuildings( ListBuildingCommand cmd) {
        ListBuildingCommandResponse buildings = communityService.listBuildings(cmd);
        RestResponse response = new RestResponse(buildings);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
