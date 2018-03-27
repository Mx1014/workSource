package com.everhomes.acl;


import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.ListCommunityRelationOfOrgIdCommand;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.acl.DistributeServiceModuleAppAuthorizationCommand;
import com.everhomes.rest.module.ListProjectIdsByAppIdAndOrganizationIdCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestDoc(value="AppAuthorization controller", site="core")
@RestController
@RequestMapping("/app/authorizaiton")
public class AppAuthorizationController {
    @Autowired
    private ServiceModuleAppAuthorizationService serviceModuleAppAuthorizationService;

    /**
     * <b>URL: /app/authorizaiton/listCommunityRelationOfOrgId</b>
     * <p>列出标准版中公司管理的所有园区</p>
     * @return List<ProjectDTO> 项目集合
     */
    @RequestMapping("listCommunityRelationOfOrgId")
    @RestReturn(value=ProjectDTO.class, collection = true)
    public RestResponse listCommunityRelationOfOrgId(@Valid ListCommunityRelationOfOrgIdCommand cmd) {
        RestResponse response = new RestResponse(serviceModuleAppAuthorizationService.listCommunityRelationOfOrgId(cmd.getNamespaceId(), cmd.getOrganizationId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /app/authorizaiton/listCommunityAppIdOfOrgId</b>
     * <p>列出标准版中公司配置了项目的所有应用</p>
     * @return List<Long> 应用id集合
     */
    @RequestMapping("listCommunityAppIdOfOrgId")
    @RestReturn(value=Long.class, collection = true)
    public RestResponse listCommunityAppIdOfOrgId(@Valid ListCommunityRelationOfOrgIdCommand cmd) {
        RestResponse response = new RestResponse(serviceModuleAppAuthorizationService.listCommunityAppIdOfOrgId(cmd.getNamespaceId(), cmd.getOrganizationId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /app/authorizaiton/listProjectIdsByAppIdAndOrganizationId</b>
     * 按应用和公司查询园区
     */
    @RequestMapping("listProjectIdsByAppIdAndOrganizationId")
    @RestReturn(value = Long.class, collection = true)
    public RestResponse listProjectIdsByAppIdAndOrganizationId(@Valid ListProjectIdsByAppIdAndOrganizationIdCommand cmd) {
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/distributeServiceModuleAppAuthorization</b>
     * 分配应用园区管理权
     */
    @RequestMapping("distributeServiceModuleAppAuthorization")
    @RestReturn(value = String.class)
    public RestResponse distributeServiceModuleAppAuthorization(@Valid DistributeServiceModuleAppAuthorizationCommand cmd) {
        serviceModuleAppAuthorizationService.distributeServiceModuleAppAuthorization(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
