package com.everhomes.acl;


import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.module.ListProjectIdsByAppIdAndOrganizationIdCommand;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
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
     * <b>URL: /app/authorizaiton/distributeServiceModuleAppAuthorization</b>
     * 分配应用园区管理权
     */
    @RequestMapping("distributeServiceModuleAppAuthorization")
    @RestReturn(value = String.class)
    public RestResponse distributeServiceModuleAppAuthorization(@Valid DistributeServiceModuleAppAuthorizationCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        serviceModuleAppAuthorizationService.distributeServiceModuleAppAuthorization(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /app/authorizaiton/createAppProfile</b>
     * 创建应用信息
     */
    @RequestMapping("createAppProfile")
    @RestReturn(value = String.class)
    public RestResponse createAppProfile(@Valid CreateAppProfileCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /app/authorizaiton/updateAppProfile</b>
     * 更新应用信息
     */
    @RequestMapping("updateAppProfile")
    @RestReturn(value = String.class)
    public RestResponse updateAppProfile(UpdateAppProfileCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        serviceModuleAppAuthorizationService.updateAppProfile(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /app/authorizaiton/getAppProfile</b>
     * 查询应用profile信息
     */
    @RequestMapping("getAppProfile")
    @RestReturn(value = ServiceModuleAppDTO.class)
    public RestResponse getAppProfile(GetAppProfileCommand cmd) {
        ServiceModuleAppDTO dto = serviceModuleAppAuthorizationService.getAppProfile(cmd);
        RestResponse response =  new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /app/authorizaiton/listAppAuthorizationsByOwnerId</b>
     * <p>获取属于该公司的应用的授权记录</p>
     */
    @RequestMapping("listAppAuthorizationsByOwnerId")
    @RestReturn(value=ListAppAuthorizationsByOwnerIdResponse.class)
    public RestResponse listAppAuthorizationsByOwnerId(@Valid ListAppAuthorizationsByOwnerIdCommand cmd) {
        ListAppAuthorizationsByOwnerIdResponse  res =  serviceModuleAppAuthorizationService.listAppAuthorizationsByOwnerId(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /app/authorizaiton/listAppAuthorizationsByOrganizatioinId</b>
     * <p>属于其他公司授权到本公司的记录，不会包括自己公司给自己公司授权的记录</p>
     */
    @RequestMapping("listAppAuthorizationsByOrganizatioinId")
    @RestReturn(value=ListAppAuthorizationsByOrganizatioinIdResponse.class)
    public RestResponse listAppAuthorizationsByOrganizatioinId(@Valid ListAppAuthorizationsByOrganizatioinIdCommand cmd) {
        ListAppAuthorizationsByOwnerIdResponse  res =  serviceModuleAppAuthorizationService.listAppAuthorizationsByOrganizatioinId(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
