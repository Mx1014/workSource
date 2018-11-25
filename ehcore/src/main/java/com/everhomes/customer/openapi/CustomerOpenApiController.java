package com.everhomes.customer.openapi;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListCommunitiesByCategoryCommand;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerResponse;
import com.everhomes.rest.customer.openapi.*;
import com.everhomes.rest.organization.pm.PropFamilyDTO;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.SignatureHelper;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/openapi/asset")
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

    @Autowired
    private AppProvider appProvider;

    /**
     * <b>URL: /asset/listCommunities</b>
     * <p>查看项目列表</p>
     */
    @RequestMapping("listCommunities")
    @RestReturn(value = String.class)
    @RequireAuthentication(value = true)
    public RestResponse listCommunities(ListCommunitiesByCategoryCommand cmd) {
        CommunityResponse resp = customerService.listCommunitiesByCategory(cmd);
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
    @RequireAuthentication(value = true)
    public RestResponse listBuildings(ListBuildingCommand cmd) {
        ListBuildingResponse buildingResponse = customerService.listBuildings(cmd);
        RestResponse response = new RestResponse(buildingResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /asset/listAddresses</b>
     * <p>查看门牌列表</p>
     */
    @RequestMapping("listAddresses")
    @RestReturn(value = PropFamilyDTO.class,collection = true)
    @RequireAuthentication(value = true)
    public RestResponse listAddresses(ListBuildingCommand cmd) {
        List<PropFamilyDTO> addresses = customerService.listAddresses(cmd.getBuildingId());
        RestResponse response = new RestResponse(addresses);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /asset/listEnterprises</b>
     * <p>查看客户列表</p>
     */
    @RequestMapping("listEnterprises")
    @RestReturn(value = SearchEnterpriseCustomerResponse.class)
    @RequireAuthentication(value = true)
    public RestResponse listEnterprises(SearchEnterpriseCustomerCommand cmd) {
        Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
        cmd.setNamespaceId(namespaceId);
        SearchEnterpriseCustomerResponse customers = enterpriseCustomerSearcher.queryEnterpriseCustomersForOpenAPI(cmd);

        List<EnterpriseDTO> dtos = new ArrayList<>();
        customers.getDtos().forEach(r ->{
            dtos.add(ConvertHelper.convert(r,EnterpriseDTO.class));
        });
        ListEnterpriseResponse responseDTO = new ListEnterpriseResponse();
        responseDTO.setResponse(dtos);
        responseDTO.setNextPageAnchor(customers.getNextPageAnchor());
        RestResponse response = new RestResponse(responseDTO);
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
    @RequireAuthentication(value = true)
    public RestResponse createEnterprise(OpenApiUpdateCustomerCommand cmd) {
        EnterpriseCustomerDTO enterpriseCustomerDTO = customerService.createEnterpriseCustomer(cmd);
        RestResponse response = new RestResponse(enterpriseCustomerDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/updateEnterprise</b>
     * <p>修改企业</p>
     */
    @RequestMapping("updateEnterprise")
    @RestReturn(value = String.class)
    @RequireAuthentication(value = true)
    public RestResponse updateEnterprise(OpenApiUpdateCustomerCommand cmd) {
        EnterpriseCustomerDTO enterpriseCustomerDTO = customerService.updateEnterpriseCustomer(cmd);
        RestResponse response = new RestResponse(enterpriseCustomerDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/deleteEnterprise</b>
     * <p>删除企业</p>
     */
    @RequestMapping("deleteEnterprise")
    @RestReturn(value = String.class)
    @RequireAuthentication(value = true)
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
    @RequireAuthentication(value = true)
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
    @RequireAuthentication(value = true)
    public RestResponse deleteEnterprisAdmin(DeleteEnterpriseCommand cmd) {
        customerService.deleteEnterpriseAdmin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/signatureGen</b>
     * <p>删除管理员</p>
     */
    @RequestMapping("signatureGen")
    @RestReturn(value = String.class)
    public RestResponse deleteEnterprisAdmin(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, String> mapForSignature = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            if (!entry.getKey().equals("signature")) {
                mapForSignature.put(entry.getKey(), StringUtils.join(entry.getValue(), ','));
            }
        }
        App app = this.appProvider.findAppByKey(mapForSignature.get("appKey"));
        RestResponse response = new RestResponse(SignatureHelper.computeSignature(mapForSignature,app.getSecretKey()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
