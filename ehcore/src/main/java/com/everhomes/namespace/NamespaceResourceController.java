// @formatter:off
package com.everhomes.namespace;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.ListOrganizationCommunityCommand;
import com.everhomes.organization.ListOrganizationCommunityCommandResponse;
import com.everhomes.organization.ListOrganizationCommunityV2CommandResponse;
import com.everhomes.region.ListRegionCommand;
import com.everhomes.region.Region;
import com.everhomes.region.RegionAdminStatus;
import com.everhomes.region.RegionDTO;
import com.everhomes.region.RegionProvider;
import com.everhomes.region.RegionScope;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.EtagHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;
import com.everhomes.util.RequireAuthentication;

/**
 * Namespace resource REST API controller
 * 
 *
 */
@RestController
@RequestMapping("/namespace")
public class NamespaceResourceController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceResourceController.class);

    @Autowired
    private NamespaceResourceService namespaceResourceService;
    
    /**
     * <b>URL: /namespace/listCommunityByNamespace</b>
     * <p>根据命名空间下列出机构所管辖的小区信息</p>
     */
    //checked
    @RequestMapping("listOrganizationCommunitiesV2")
    @RestReturn(value=ListOrganizationCommunityCommandResponse.class)
    public RestResponse listCommunityByNamespace(@Valid ListCommunityByNamespaceCommand cmd) {
        ListCommunityByNamespaceCommandResponse commandResponse = namespaceResourceService.listCommunityByNamespace(cmd);
        
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
